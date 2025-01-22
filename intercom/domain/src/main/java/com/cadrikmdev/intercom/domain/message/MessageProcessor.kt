package com.cadrikmdev.intercom.domain.message

import com.cadrikmdev.intercom.domain.client.TrackingDevice

interface MessageProcessor {
    fun processMessage(message: String?): MessageAction?

    fun sendAction(action: MessageAction?): String?

    fun processConnectedDevice(name: String?, address: String): TrackingDevice
}