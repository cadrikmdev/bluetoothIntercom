package com.cadrikmdev.intercom.data.message

import com.cadrikmdev.intercom.domain.message.MessageAction

fun TrackerActionDto.toTrackerAction(): MessageAction {
    return when (this) {
        is TrackerActionDto.StartTest -> MessageAction.StartTest(address)
        is TrackerActionDto.StopTest -> MessageAction.StopTest(address)
        is TrackerActionDto.UpdateProgress -> MessageAction.UpdateProgress(progress)
    }
}

fun MessageAction.toTrackerActionDto(): TrackerActionDto {
    return when (this) {
        is MessageAction.StartTest -> TrackerActionDto.StartTest(address)
        is MessageAction.StopTest -> TrackerActionDto.StopTest(address)
        is MessageAction.UpdateProgress -> TrackerActionDto.UpdateProgress(progress)
    }
}