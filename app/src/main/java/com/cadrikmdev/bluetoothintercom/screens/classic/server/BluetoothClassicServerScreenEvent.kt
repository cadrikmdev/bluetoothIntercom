package com.cadrikmdev.bluetoothintercom.screens.classic.server

import com.cadrikmdev.bluetoothintercom.screens.classic.server.BluetoothClassicServerScreenEvent

sealed interface BluetoothClassicServerScreenEvent {
    data object OnResumed: BluetoothClassicServerScreenEvent
}