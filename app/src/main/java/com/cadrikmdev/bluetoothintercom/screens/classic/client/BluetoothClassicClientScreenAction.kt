package com.cadrikmdev.bluetoothintercom.screens.classic.client

sealed interface BluetoothClassicClientScreenAction {
    data class ConnectToDeviceClicked(val address: String): BluetoothClassicClientScreenAction
    data class DisconnectToDeviceClicked(val address: String): BluetoothClassicClientScreenAction
}