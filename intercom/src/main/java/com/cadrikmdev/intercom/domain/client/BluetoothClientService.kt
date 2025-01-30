package com.cadrikmdev.intercom.domain.client

import com.cadrikmdev.intercom.domain.util.Result
import com.cadrikmdev.intercom.domain.message.MessageWrapper
import kotlinx.coroutines.flow.MutableStateFlow

interface BluetoothClientService {

    val trackingDevices: MutableStateFlow<Map<String, TrackingDevice>>

    val sendActionFlow: MutableStateFlow<MessageWrapper?>

    suspend fun connectToDevice(deviceAddress: String): Result<Boolean, BluetoothError>

    /**
     * Returns true if disconnecting was successfully completed or device is already disconnected
     */
    fun disconnectFromDevice(deviceAddress: String): Boolean
}