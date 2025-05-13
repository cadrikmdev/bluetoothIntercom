package com.cadrikmdev.bluetoothintercom.screens.classic.client.state

import com.cadrikmdev.bluetoothintercom.screens.classic.client.model.BluetoothDeviceState
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
        val mapDevicesStates = devices.associateWith {
            BluetoothDeviceState(it)
        }
        mutableStateFlow.update {
            it.copy(
                devices = devices,
                devicesStates = mapDevicesStates
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

    fun updateDeviceState() {
//        mutableStateFlow.update {
////            it.copy(
////                devices = devices,
////                devicesStates = mapDevicesStates
////            )
//        }
    }

    fun getDeviceState(sourceDevice: BluetoothDevice): BluetoothDeviceState {
        val deviceKey: BluetoothDevice? = state.devicesStates.keys.firstOrNull { bluetoothDevice ->
            bluetoothDevice.address == sourceDevice.address
        }
        val defaultState = BluetoothDeviceState(sourceDevice)
        if (deviceKey == null) {
            return defaultState
        }
        val existingDeviceState: BluetoothDeviceState? = state.devicesStates[deviceKey]
        return  existingDeviceState ?: BluetoothDeviceState(sourceDevice)
    }
}