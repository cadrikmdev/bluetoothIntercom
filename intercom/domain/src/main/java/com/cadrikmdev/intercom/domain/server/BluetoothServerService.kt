package com.cadrikmdev.intercom.domain.server

import com.cadrikmdev.intercom.domain.data.MessageContent
import com.cadrikmdev.intercom.domain.message.MessageAction
import kotlinx.coroutines.flow.SharedFlow

interface BluetoothServerService {

    val receivedActionFlow: SharedFlow<MessageAction?>

    fun startServer()

    fun stopServer()

    fun setMeasurementProgressCallback(statusUpdate: () -> MessageContent?)
}