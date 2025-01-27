package com.cadrikmdev.intercom.domain.data

import com.cadrikmdev.intercom.domain.message.SerializableContent
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.Serializable

@Serializable
data class MessageContent<T : SerializableContent>(
    @Polymorphic val content: T,
    val timestamp: Long,
)

@Serializable
data class TextContent(val text: String) : SerializableContent()