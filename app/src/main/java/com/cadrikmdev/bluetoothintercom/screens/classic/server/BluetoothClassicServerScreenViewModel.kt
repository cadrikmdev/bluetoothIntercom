package com.cadrikmdev.bluetoothintercom.screens.classic.server

import androidx.lifecycle.ViewModel
import com.cadrikmdev.bluetoothintercom.screens.classic.server.state.BluetoothClassicServerScreenStateManager
import com.cadrikmdev.intercom.domain.server.BluetoothServerService

class BluetoothClassicServerScreenViewModel(
    private val bluetoothClassicIntercomServer: BluetoothServerService
): ViewModel() {

    private val stateManager = BluetoothClassicServerScreenStateManager()

    val stateFlow
        get() = this.stateManager.stateFlow

    fun onEvent(event: BluetoothClassicServerScreenEvent) {
        when (event) {
            BluetoothClassicServerScreenEvent.OnResumed -> TODO()
        }
    }

    fun onAction(action: BluetoothClassicServerScreenAction) {
        when (action) {
            BluetoothClassicServerScreenAction.StartActionClicked -> startBluetoothServer()
            BluetoothClassicServerScreenAction.StopActionClicked -> stopBluetoothServer()
        }
    }

    private fun startBluetoothServer() {
        bluetoothClassicIntercomServer.startServer()
    }

    private fun stopBluetoothServer() {
        bluetoothClassicIntercomServer.stopServer()
    }
}
