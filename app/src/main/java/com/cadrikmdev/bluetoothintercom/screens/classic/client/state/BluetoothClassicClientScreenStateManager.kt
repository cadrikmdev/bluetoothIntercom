package com.cadrikmdev.bluetoothintercom.screens.classic.client.state

import com.cadrikmdev.intercom.domain.data.BluetoothDevice
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class BluetoothClassicClientScreenStateManager {

    protected val mutableStateFlow: MutableStateFlow<BluetoothClassicClientScreenState> by lazy { MutableStateFlow(this.setInitialState()) }
    val stateFlow = this.mutableStateFlow.asStateFlow()

    val state
        get() = stateFlow.value

    fun setInitialState(): BluetoothClassicClientScreenState {
        return BluetoothClassicClientScreenState()
    }

    fun setPairedDevices(devices: List<BluetoothDevice>) {
        mutableStateFlow.update {
            it.copy(
                devices = devices
            )
        }
    }
}