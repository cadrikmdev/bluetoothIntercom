package com.cadrikmdev.bluetoothintercom.screens.classic.client

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cadrikmdev.bluetoothintercom.screens.classic.client.state.BluetoothClassicClientScreenStateManager
import com.cadrikmdev.intercom.domain.client.BluetoothClientService
import kotlinx.coroutines.launch

class BluetoothClassicClientScreenViewModel(
    private val bluetoothClassicClientService: BluetoothClientService
): ViewModel() {

    private val stateManager = BluetoothClassicClientScreenStateManager()

    val stateFlow
        get() = this.stateManager.stateFlow

    fun onEvent(event: BluetoothClassicClientScreenEvent) {
        when (event) {
            BluetoothClassicClientScreenEvent.OnResumed -> {}
        }
    }

    fun onAction(action: BluetoothClassicClientScreenAction) {
        when (action) {
            is BluetoothClassicClientScreenAction.ConnectToDeviceClicked -> connectToDevice(action.address)
            is BluetoothClassicClientScreenAction.DisconnectToDeviceClicked -> disconnectFromDevice(action.address)
        }
    }

    private fun connectToDevice(address: String) {
        viewModelScope.launch {
            bluetoothClassicClientService.connectToDevice(address)
        }
    }

    private fun disconnectFromDevice(address: String) {
        bluetoothClassicClientService.disconnectFromDevice(address)
    }
}
