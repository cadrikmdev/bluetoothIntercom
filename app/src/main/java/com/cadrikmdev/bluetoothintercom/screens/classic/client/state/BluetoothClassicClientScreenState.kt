package com.cadrikmdev.bluetoothintercom.screens.classic.client.state

import com.cadrikmdev.bluetoothintercom.screens.classic.client.model.BluetoothDeviceState
import com.cadrikmdev.intercom.domain.data.BluetoothDevice

data class BluetoothClassicClientScreenState(
    val isBluetoothAdapterEnabled: Boolean = false,
    val devices: List<BluetoothDevice> = emptyList(),
    val devicesStates: Map<BluetoothDevice, BluetoothDeviceState> = emptyMap()
)
