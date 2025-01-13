package com.cadrikmdev.bluetoothintercom.screens.classic.server

import com.cadrikmdev.bluetoothintercom.screens.classic.client.BluetoothClassicServerScreenEvent

sealed interface BluetoothClassicServerScreenEvent {
    data object OnResumed: BluetoothClassicServerScreenEvent
}