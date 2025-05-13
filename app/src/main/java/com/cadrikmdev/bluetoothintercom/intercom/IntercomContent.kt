package com.cadrikmdev.bluetoothintercom.intercom

import com.cadrikmdev.intercom.domain.message.SerializableContent
import kotlinx.serialization.Serializable

@Serializable
data object StartContent : SerializableContent()

@Serializable
data object StopContent : SerializableContent()