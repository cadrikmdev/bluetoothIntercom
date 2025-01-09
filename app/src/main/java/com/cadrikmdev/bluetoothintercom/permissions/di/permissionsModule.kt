package com.cadrikmdev.bluetoothintercom.permissions.di

import com.cadrikmdev.bluetoothintercom.permissions.appPermissions
import com.cadrikmdev.permissions.domain.PermissionHandler
import com.cadrikmdev.permissions.presentation.screen.permissions.PermissionHandlerImpl
import com.cadrikmdev.permissions.presentation.screen.permissions.PermissionsScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val permissionHandlerModule = module {
    single<PermissionHandler> {
        val handler = PermissionHandlerImpl(
            get()
        )
        handler.setPermissionsNeeded(appPermissions)
        handler
    }

    viewModelOf(::PermissionsScreenViewModel)
}