package com.cadrikmdev.intercom.domain.message

import com.cadrikmdev.intercom.domain.data.MessageContent

sealed interface Message {
    data object StartMeasurement : Message
    data object StopMeasurement : Message
    data class MeasurementStatus(val progress: MessageContent<SerializableContent>) : Message
}
