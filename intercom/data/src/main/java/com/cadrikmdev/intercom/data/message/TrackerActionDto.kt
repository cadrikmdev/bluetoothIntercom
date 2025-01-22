package com.cadrikmdev.intercom.data.message

import com.cadrikmdev.intercom.domain.data.MessageContent
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Polymorphic
sealed class TrackerActionDto {
    @Serializable
    @SerialName("StartTest")
    data class StartTest(val address: String) : TrackerActionDto()
    @Serializable
    @SerialName("StopTest")
    data class StopTest(val address: String) : TrackerActionDto()
    @Serializable
    @SerialName("UpdateProgress")
    data class UpdateProgress(val progress: MessageContent) : TrackerActionDto()
}