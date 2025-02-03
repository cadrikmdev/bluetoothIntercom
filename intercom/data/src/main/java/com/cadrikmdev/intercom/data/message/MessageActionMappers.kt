package com.cadrikmdev.intercom.data.message

import com.cadrikmdev.intercom.domain.message.MessageWrapper

fun MessageActionDto.toMessageAction(): MessageWrapper {
    return when (this) {
        is MessageActionDto.SendMessage -> MessageWrapper.SendMessage(address, content)
    }
}

fun MessageWrapper.toTrackerActionDto(): MessageActionDto? {
    return when (this) {
        is MessageWrapper.SendMessage -> MessageActionDto.SendMessage(address, content)
        else -> {
            null
        }
    }
}