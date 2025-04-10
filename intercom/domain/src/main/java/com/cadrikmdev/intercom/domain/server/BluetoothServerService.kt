package com.cadrikmdev.intercom.domain.server

import com.cadrikmdev.intercom.domain.data.MessageContent
import com.cadrikmdev.intercom.domain.message.MessageWrapper
import com.cadrikmdev.intercom.domain.message.SerializableContent
import kotlinx.coroutines.flow.SharedFlow

interface BluetoothServerService {

    val connectionStateFlow: SharedFlow<ConnectionState>

    fun startServer()

    fun stopServer()

    fun setMeasurementProgressCallback(statusUpdate: () -> MessageContent<SerializableContent>?)
}