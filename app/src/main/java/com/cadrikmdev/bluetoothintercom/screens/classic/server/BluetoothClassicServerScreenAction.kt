package com.cadrikmdev.bluetoothintercom.screens.classic.server

import com.cadrikmdev.bluetoothintercom.screens.classic.client.BluetoothClassicServerScreenAction

sealed interface BluetoothClassicServerScreenAction {
    data object StartActionClicked: BluetoothClassicServerScreenAction
    data object StopActionClicked: BluetoothClassicServerScreenAction
}