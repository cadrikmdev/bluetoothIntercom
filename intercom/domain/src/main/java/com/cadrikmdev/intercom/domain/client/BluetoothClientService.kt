package com.cadrikmdev.intercom.domain.client

import com.cadrikmdev.intercom.domain.data.BluetoothDevice
import com.cadrikmdev.intercom.domain.util.Result
import com.cadrikmdev.intercom.domain.message.MessageWrapper
import kotlinx.coroutines.flow.MutableStateFlow

interface BluetoothClientService {

    val pairedBluetoothDevices: MutableStateFlow<Map<String, BluetoothDevice>>

    val sendActionFlow: MutableStateFlow<MessageWrapper?>

    val receivedActionFlow: MutableStateFlow<MessageWrapper?>

    suspend fun connectToDevice(deviceAddress: String): Result<Boolean, BluetoothError>

    /**
     * Returns true if disconnecting was successfully completed or device is already disconnected
     */
    fun disconnectFromDevice(deviceAddress: String): Boolean
}