package com.cadrikmdev.intercom.data

import com.cadrikmdev.intercom.data.message.MessageActionDto
import com.cadrikmdev.intercom.data.message.toMessageAction
import com.cadrikmdev.intercom.data.message.toMessageActionDto
import com.cadrikmdev.intercom.domain.data.BluetoothDevice
import com.cadrikmdev.intercom.domain.data.TextContent
import com.cadrikmdev.intercom.domain.message.MessageProcessor
import com.cadrikmdev.intercom.domain.message.MessageWrapper
import com.cadrikmdev.intercom.domain.message.SerializableContent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder
import kotlinx.serialization.modules.polymorphic

class AndroidMessageProcessor(
    otherSerializers: SerializersModule? = null
) : MessageProcessor {

    private val _receivedMessageFlow = MutableSharedFlow<MessageWrapper?>()
    override val receivedMessageFlow: SharedFlow<MessageWrapper?> get() = _receivedMessageFlow

    val json = Json {
        serializersModule = SerializersModule {
            basicSerializer()
            include(otherSerializers ?: SerializersModule {})
        }
        classDiscriminator = "type"
        encodeDefaults = true
    }

    private fun SerializersModuleBuilder.basicSerializer() {
        polymorphic(SerializableContent::class) {
            subclass(TextContent::class, TextContent.serializer())
        }
    }

    override suspend fun processMessageFrom(device: BluetoothDevice, message: String?): MessageWrapper? {
        try {
            val action = message?.let { mess ->
                json.decodeFromString<MessageActionDto>(mess).toMessageAction(device)
            }
            action?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    launch(Dispatchers.IO) {
                        _receivedMessageFlow.emit(action)
                    }
                }
            }
            return action
        } catch (e: SerializationException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        return null
    }

    override fun sendAction(action: MessageWrapper?): String? {
        try {
            action?.let {
                return json.encodeToString(it.toMessageActionDto()) + "\n"
            }
        } catch (e: SerializationException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        return null
    }

    override fun processConnectedDevice(name: String?, address: String): BluetoothDevice {
        return BluetoothDevice(
            address = address,
            displayName = name ?: "Unknown",
            connected = false,
            lastUpdatedTimestamp = System.currentTimeMillis(),
        )
    }
}