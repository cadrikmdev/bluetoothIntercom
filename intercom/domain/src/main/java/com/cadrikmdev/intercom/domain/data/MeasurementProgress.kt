package com.cadrikmdev.intercom.domain.data

import kotlinx.serialization.Serializable

@Serializable
data class MessageContent(
    val content: java.io.Serializable,
    val timestamp: Long,
)
