package com.cadrikmdev.intercom.data.message

import com.cadrikmdev.intercom.domain.message.MessageWrapper

fun TrackerActionDto.toTrackerAction(): MessageWrapper {
    return when (this) {
        is TrackerActionDto.SendMessage -> MessageWrapper.SendMessage(address, content)
    }
}

fun MessageWrapper.toTrackerActionDto(): TrackerActionDto? {
    return when (this) {
        is MessageWrapper.SendMessage -> TrackerActionDto.SendMessage(address, content)
        else -> {
            null
        }
    }
}