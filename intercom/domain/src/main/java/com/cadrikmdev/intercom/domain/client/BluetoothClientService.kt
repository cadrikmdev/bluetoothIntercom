package com.cadrikmdev.intercom.domain.client

import com.cadrikmdev.core.domain.util.Result
import com.cadrikmdev.intercom.domain.message.MessageAction
import kotlinx.coroutines.flow.MutableStateFlow

interface BluetoothClientService {

    val trackingDevices: MutableStateFlow<Map<String, TrackingDevice>>

    val sendActionFlow: MutableStateFlow<MessageAction?>

    suspend fun connectToDevice(deviceAddress: String): Result<Boolean, BluetoothError>

    /**
     * Returns true if disconnecting was successfully completed or device is already disconnected
     */
    fun disconnectFromDevice(deviceAddress: String): Boolean
}