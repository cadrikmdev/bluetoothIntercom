package com.cadrikmdev.intercom.data.message

import com.cadrikmdev.intercom.domain.data.MessageContent
import com.cadrikmdev.intercom.domain.message.SerializableContent
import kotlinx.serialization.Contextual
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Polymorphic
sealed class TrackerActionDto {
    @Serializable
    @SerialName("SendMessage")
    data class SendMessage(val address: String, val content: @Contextual MessageContent<SerializableContent>) : TrackerActionDto()
}