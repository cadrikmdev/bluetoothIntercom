package com.cadrikmdev.bluetoothintercom.screens.classic.server

sealed interface BluetoothClassicServerScreenAction {
    data object StartActionClicked: BluetoothClassicServerScreenAction
    data object StopActionClicked: BluetoothClassicServerScreenAction
}