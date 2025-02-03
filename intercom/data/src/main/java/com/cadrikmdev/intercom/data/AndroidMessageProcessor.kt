package com.cadrikmdev.intercom.data

import com.cadrikmdev.intercom.data.message.MessageActionDto
import com.cadrikmdev.intercom.data.message.toMessageAction
import com.cadrikmdev.intercom.data.message.toTrackerActionDto
import com.cadrikmdev.intercom.domain.client.TrackingDevice
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
import kotlinx.serialization.modules.polymorphic

class AndroidMessageProcessor(

) : MessageProcessor {

    private val _receivedMessageFlow = MutableSharedFlow<MessageWrapper?>()
    override val receivedMessageFlow: SharedFlow<MessageWrapper?> get() = _receivedMessageFlow

    val json = Json {
        serializersModule = SerializersModule {
            polymorphic(SerializableContent::class) {
                subclass(TextContent::class, TextContent.serializer())
            }
        }
        classDiscriminator = "type"
        encodeDefaults = true
    }

    override suspend fun processMessageFrom(address: String, message: String?): MessageWrapper? {
        try {
            val action = message?.let { mess ->
                json.decodeFromString<MessageActionDto>(mess).toMessageAction()
            }
            action?.let {
                CoroutineScope(Dispatchers.IO).launch {
                    launch(Dispatchers.IO) {
                        _receivedMessageFlow.emit(action)
                    }
                }
            }
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
                return json.encodeToString(it.toTrackerActionDto()) + "\n"
            }
        } catch (e: SerializationException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        return null
    }

    override fun processConnectedDevice(name: String?, address: String): TrackingDevice {
        return TrackingDevice(
            address = address,
            name = name ?: "Unknown",
            connected = false,
            updateTimestamp = System.currentTimeMillis(),
        )
    }
}