package com.cadrikmdev.bluetoothintercom.screens.classic.client

sealed interface BluetoothClassicClientScreenEvent {
    data object OnResumed: BluetoothClassicClientScreenEvent
}