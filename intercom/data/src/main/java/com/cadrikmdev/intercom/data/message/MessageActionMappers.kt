package com.cadrikmdev.intercom.data.message

import com.cadrikmdev.intercom.domain.data.BluetoothDevice
import com.cadrikmdev.intercom.domain.message.MessageWrapper

fun MessageActionDto.toMessageAction(sourceDevice: BluetoothDevice): MessageWrapper {
    return when (this) {
        is MessageActionDto.SendMessage -> MessageWrapper.SendMessage(sourceDevice, content)
    }
}

fun MessageWrapper.toMessageActionDto(): MessageActionDto? {
    return when (this) {
        is MessageWrapper.SendMessage -> MessageActionDto.SendMessage(sourceDevice.address, content)
        else -> {
            null
        }
    }
}