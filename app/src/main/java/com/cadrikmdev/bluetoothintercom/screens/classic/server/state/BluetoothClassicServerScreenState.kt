package com.cadrikmdev.bluetoothintercom.screens.classic.server.state

import com.cadrikmdev.intercom.domain.server.ConnectionState

data class BluetoothClassicServerScreenState(
    val isServerRunning: Boolean = false,
    val isBluetoothAdapterEnabled: Boolean = false,
    val intercomStatus: ConnectionState = ConnectionState.STOPPED
)
