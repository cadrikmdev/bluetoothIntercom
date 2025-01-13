package com.cadrikmdev.bluetoothintercom.screens.classic.server

import androidx.lifecycle.ViewModel
import com.cadrikmdev.bluetoothintercom.screens.classic.server.state.BluetoothClassicServerScreenStateManager

class BluetoothClassicServerScreenViewModel: ViewModel() {

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
            BluetoothClassicServerScreenAction.StartActionClicked -> TODO()
            BluetoothClassicServerScreenAction.StopActionClicked -> TODO()
        }
    }
}
