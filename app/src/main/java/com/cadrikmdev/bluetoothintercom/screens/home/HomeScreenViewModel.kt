package com.cadrikmdev.bluetoothintercom.screens.home

import androidx.lifecycle.ViewModel
import com.cadrikmdev.bluetoothintercom.screens.home.state.HomeScreenStateManager
import com.cadrikmdev.permissions.domain.PermissionHandler

class HomeScreenViewModel(
    private val permissionHandler: PermissionHandler,
): ViewModel() {

    private val stateManager = HomeScreenStateManager()

    val stateFlow
        get() = this.stateManager.stateFlow

    fun onAction(action: HomeScreenAction) {

    }

    fun onEvent(event: HomeScreenEvent) {
        when (event) {
            HomeScreenEvent.OnResumed -> performOnResumeChecks()
        }
    }

    private fun performOnResumeChecks() {
        permissionHandler.checkPermissionsState()
        val allPermissionsGranted = permissionHandler.getNotGrantedPermissionList().isEmpty()
        stateManager.setAllPermissionsGranted(allPermissionsGranted)
    }
}