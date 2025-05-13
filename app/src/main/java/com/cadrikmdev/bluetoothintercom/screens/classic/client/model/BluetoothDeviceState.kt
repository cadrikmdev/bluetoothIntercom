package com.cadrikmdev.bluetoothintercom.screens.classic.client.model

import com.cadrikmdev.intercom.domain.data.BluetoothDevice

data class BluetoothDeviceState(
    val bluetoothDevice: BluetoothDevice,
    val started: Boolean = false,
    val lastUpdateTimestamp: Long = System.currentTimeMillis(),
)

