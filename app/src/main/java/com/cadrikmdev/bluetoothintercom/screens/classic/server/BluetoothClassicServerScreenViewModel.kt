package com.cadrikmdev.bluetoothintercom.screens.classic.server

import androidx.lifecycle.viewModelScope
import com.cadrikmdev.bluetoothintercom.base.BaseViewModel
import com.cadrikmdev.bluetoothintercom.screens.classic.server.state.BluetoothClassicServerScreenStateManager
import com.cadrikmdev.intercom.domain.data.MessageContent
import com.cadrikmdev.intercom.domain.server.BluetoothServerService
import com.cadrikmdev.intercom.domain.service.BluetoothService
import com.cadrikmdev.permissions.domain.PermissionHandler
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class BluetoothClassicServerScreenViewModel(
    private val bluetoothClassicIntercomServer: BluetoothServerService,
    private val androidBluetoothService: BluetoothService,
    private val permissionHandler: PermissionHandler,
): BaseViewModel() {

    private val stateManager = BluetoothClassicServerScreenStateManager()

    val stateFlow
        get() = this.stateManager.stateFlow

    init {
        bluetoothClassicIntercomServer.connectionStateFlow.onEach {
            stateManager.updateIntercomState(it)
        }.launchIn(viewModelScope)
    }

    fun onEvent(event: BluetoothClassicServerScreenEvent) {
        when (event) {
            BluetoothClassicServerScreenEvent.OnResumed -> {
                stateManager.updateIsBluetoothEnabled(androidBluetoothService.isBluetoothEnabled())
            }
        }
    }

    fun onAction(action: BluetoothClassicServerScreenAction) {
        when (action) {
            BluetoothClassicServerScreenAction.StartActionClicked -> startBluetoothServer()
            BluetoothClassicServerScreenAction.StopActionClicked -> stopBluetoothServer()
            BluetoothClassicServerScreenAction.OnOpenBluetoothSettingsClick -> {
                androidBluetoothService.openBluetoothSettings()
            }
        }
    }

    private fun startBluetoothServer() {
        startIntercomServices()
    }

    private fun stopBluetoothServer() {
        bluetoothClassicIntercomServer.stopServer()
    }

    fun startIntercomServices() {
        viewModelScope.launch {
            bluetoothClassicIntercomServer.startServer()
            bluetoothClassicIntercomServer.setMeasurementProgressCallback {
                MessageContent(
                    content = "update message",
                    timestamp = System.currentTimeMillis(),
                )
            }
            bluetoothClassicIntercomServer.receivedMessageFlow.onEach { action ->
//                when (action) {
//                    is TrackerAction.StartTest -> {
//                        stopObserving()
//                        clearData()
//                        startObserving()
//                        _isTracking.emit(true)
//                        _trackActions.emit(TrackerAction.StartTest(""))
//                        println("Emitting start action in Tracker")
//                    }
//
//                    is TrackerAction.StopTest -> {
//                        _isTracking.emit(false)
//                        _trackActions.emit(TrackerAction.StopTest(""))
//                        // we can clear all data, because they are already in DB
//                        clearData()
//                        println("Emitting stop action in Tracker")
//                    }
//
//                    else -> Unit
//                }
            }.launchIn(viewModelScope)
        }
    }
}
