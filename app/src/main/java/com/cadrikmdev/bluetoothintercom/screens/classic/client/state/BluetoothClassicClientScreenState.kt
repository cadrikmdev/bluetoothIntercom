package com.cadrikmdev.bluetoothintercom.screens.classic.client.state

import com.cadrikmdev.intercom.domain.client.TrackingDevice

data class BluetoothClassicClientScreenState(
    val isBluetoothAdapterEnabled: Boolean = false,
    val devices: List<TrackingDevice> = emptyList()
)
