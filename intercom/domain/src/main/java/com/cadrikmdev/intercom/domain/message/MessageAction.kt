package com.cadrikmdev.intercom.domain.message

import com.cadrikmdev.intercom.domain.data.MessageContent
import kotlinx.serialization.Serializable

@Serializable
sealed interface MessageAction {
    @Serializable
    data class StartTest(val address: String) : MessageAction
    @Serializable
    data class StopTest(val address: String) : MessageAction
    @Serializable
    data class UpdateProgress(val progress: MessageContent) : MessageAction
}