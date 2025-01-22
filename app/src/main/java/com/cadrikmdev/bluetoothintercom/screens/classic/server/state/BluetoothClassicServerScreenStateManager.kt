package com.cadrikmdev.bluetoothintercom.screens.classic.server.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BluetoothClassicServerScreenStateManager {

    protected val mutableStateFlow: MutableStateFlow<BluetoothClassicServerScreenState> by lazy { MutableStateFlow(this.setInitialState()) }
    val stateFlow = this.mutableStateFlow.asStateFlow()

    val state
        get() = stateFlow.value

    fun setInitialState(): BluetoothClassicServerScreenState {
        return BluetoothClassicServerScreenState()
    }

    fun setIsServerRunning(allPermissionsGranted: Boolean) {
        mutableStateFlow.update {
            it.copy(
                isServerRunning = allPermissionsGranted
            )
        }
    }

    fun updateIsBluetoothEnabled(bluetoothEnabled: Boolean) {
        mutableStateFlow.update {
            it.copy(
                isBluetoothAdapterEnabled = bluetoothEnabled
            )
        }
    }
}