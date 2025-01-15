package com.cadrikmdev.bluetoothintercom.screens.classic.client.state

import com.cadrikmdev.intercom.domain.data.BluetoothDevice

data class BluetoothClassicClientScreenState(
    val devices: List<BluetoothDevice> = emptyList()
)
