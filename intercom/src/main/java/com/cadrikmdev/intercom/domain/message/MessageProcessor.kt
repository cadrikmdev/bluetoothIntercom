package com.cadrikmdev.intercom.domain.message

import com.cadrikmdev.intercom.domain.client.TrackingDevice
import kotlinx.coroutines.flow.SharedFlow

interface MessageProcessor {
    val receivedMessageFlow: SharedFlow<MessageWrapper?>

    suspend fun processMessageFrom(address: String, message: String?): MessageWrapper?

    fun sendAction(action: MessageWrapper?): String?

    fun processConnectedDevice(name: String?, address: String): TrackingDevice
}