package com.cadrikmdev.intercom.data

import com.cadrikmdev.intercom.data.message.TrackerActionDto
import com.cadrikmdev.intercom.data.message.toTrackerAction
import com.cadrikmdev.intercom.data.message.toTrackerActionDto
import com.cadrikmdev.intercom.domain.client.TrackingDevice
import com.cadrikmdev.intercom.domain.message.MessageProcessor
import com.cadrikmdev.intercom.domain.message.MessageAction
import kotlinx.serialization.SerializationException
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic
import kotlinx.serialization.modules.subclass

class AndroidMessageProcessor(

) : MessageProcessor {

    val json = Json {
        serializersModule = SerializersModule {
            polymorphic(TrackerActionDto::class) {
                subclass(TrackerActionDto.StartTest::class)
                subclass(TrackerActionDto.StopTest::class)
                subclass(TrackerActionDto.UpdateProgress::class)
            }
        }
        classDiscriminator = "type"
        encodeDefaults = true
    }

    override fun processMessage(message: String?): MessageAction? {
        try {
            message?.let { mess ->
                return json.decodeFromString<TrackerActionDto>(mess).toTrackerAction()
            }
        } catch (e: SerializationException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
        return null
    }

    override fun sendAction(action: MessageAction?): String? {
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