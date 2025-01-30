package com.cadrikmdev.bluetoothintercom.screens.classic.client

import android.Manifest
import android.bluetooth.BluetoothDevice
import androidx.lifecycle.viewModelScope
import com.cadrikmdev.bluetoothintercom.base.BaseViewModel
import com.cadrikmdev.bluetoothintercom.screens.classic.client.state.BluetoothClassicClientScreenStateManager
import com.cadrikmdev.intercom.domain.BluetoothDevicesProvider
import com.cadrikmdev.intercom.domain.client.BluetoothClientService
import com.cadrikmdev.intercom.domain.client.DeviceType
import com.cadrikmdev.intercom.domain.data.MessageContent
import com.cadrikmdev.intercom.domain.data.TextContent
import com.cadrikmdev.intercom.domain.message.MessageProcessor
import com.cadrikmdev.intercom.domain.message.MessageWrapper
import com.cadrikmdev.intercom.domain.message.SerializableContent
import com.cadrikmdev.intercom.domain.service.BluetoothService
import com.cadrikmdev.permissions.domain.PermissionHandler
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class BluetoothClassicClientScreenViewModel(
    private val bluetoothClassicClientService: BluetoothClientService,
    private val bluetoothDevicesProvider: com.cadrikmdev.intercom.domain.BluetoothDevicesProvider<BluetoothDevice>,
    private val androidBluetoothService: BluetoothService,
    private val messageProcessor: MessageProcessor,
    private val permissionHandler: PermissionHandler,
): BaseViewModel() {

    private val stateManager = BluetoothClassicClientScreenStateManager()

    val stateFlow
        get() = this.stateManager.stateFlow

    fun onEvent(event: BluetoothClassicClientScreenEvent) {
        when (event) {
            BluetoothClassicClientScreenEvent.OnResumed -> {
                stateManager.updateIsBluetoothEnabled(androidBluetoothService.isBluetoothEnabled())
                manageBluetoothDevices()
            }
        }
    }

    fun onAction(action: BluetoothClassicClientScreenAction) {
        when (action) {
            is BluetoothClassicClientScreenAction.ConnectToDeviceClicked -> connectToDevice(action.address)
            is BluetoothClassicClientScreenAction.DisconnectToDeviceClicked -> disconnectFromDevice(action.address)
            BluetoothClassicClientScreenAction.OnOpenBluetoothSettingsClick -> {
                androidBluetoothService.openBluetoothSettings()
            }
            is BluetoothClassicClientScreenAction.SendMessageToDevice -> {
                viewModelScope.launch {
                    when (action.messageToDevice) {
                        "start" -> bluetoothClassicClientService.sendActionFlow.emit(
                            MessageWrapper.SendMessage(
                                address = action.address,
                                content = MessageContent<SerializableContent>(
                                    content = TextContent("Start action send from bluetooth classic client view model"),
                                    timestamp = System.currentTimeMillis()
                                )
                            )
                        )
                        "stop" -> bluetoothClassicClientService.sendActionFlow.emit(
                            MessageWrapper.SendMessage(
                                address = action.address,
                                content = MessageContent<SerializableContent>(
                                    content = TextContent("Stop action send from bluetooth classic client view model"),
                                    timestamp = System.currentTimeMillis()
                                )
                            )
                        )
                    }
                }
            }
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

    private fun manageBluetoothDevices() {
        if (permissionHandler.isPermissionGranted(Manifest.permission.BLUETOOTH_CONNECT)) {

            val pairedDevicesFlow = bluetoothDevicesProvider.observePairedDevices(DeviceType.WORKER)

//            pairedDevicesFlow.onEach { pairedDevices ->
//                stateManager.setPairedDevices(
//                    pairedDevices.values.map { it.toTrackingDevice() }.filterNotNull().toList()
//                )
//            }.launchIn(viewModelScope)

            bluetoothClassicClientService.trackingDevices
                .onEach { devices ->
                    stateManager.setPairedDevices(
                       devices.values.toList()
                    )
                }.launchIn(viewModelScope)
        }


    }
}
