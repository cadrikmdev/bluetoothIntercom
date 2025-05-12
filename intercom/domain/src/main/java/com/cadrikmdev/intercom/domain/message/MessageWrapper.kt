package com.cadrikmdev.intercom.domain.message

import com.cadrikmdev.intercom.domain.data.BluetoothDevice
import com.cadrikmdev.intercom.domain.data.MessageContent
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable


@Serializable
@Polymorphic
abstract class SerializableContent

@Serializable
abstract class MessageWrapper(
    val destinationAddress: String
) {
    @Serializable
    data class SendMessage(val sourceDevice: BluetoothDevice, val content: MessageContent<SerializableContent>) : MessageWrapper(sourceDevice.address)
}