package com.cadrikmdev.bluetoothintercom.screens.home.state

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class HomeScreenStateManager {

    protected val mutableStateFlow: MutableStateFlow<HomeScreenState> by lazy { MutableStateFlow(this.setInitialState()) }
    val stateFlow = this.mutableStateFlow.asStateFlow()

    val state
        get() = stateFlow.value

    fun setInitialState(): HomeScreenState {
        return HomeScreenState()
    }

    fun setAllPermissionsGranted(allPermissionsGranted: Boolean) {
        mutableStateFlow.update {
            it.copy(
                allPermissionsGranted = allPermissionsGranted
            )
        }
    }
}