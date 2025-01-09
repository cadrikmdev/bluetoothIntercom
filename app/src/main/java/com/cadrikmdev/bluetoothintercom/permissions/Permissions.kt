package com.cadrikmdev.bluetoothintercom.permissions

import android.Manifest
import android.os.Build
import com.cadrikmdev.permissions.domain.model.Permission
import com.cadrikmdev.permissions.presentation.model.AndroidPermission

val appPermissions = linkedMapOf<String, Permission>(
    Pair(
        Manifest.permission.BLUETOOTH_CONNECT,
        AndroidPermission(
            name = Manifest.permission.BLUETOOTH_CONNECT,
            minimumApiRequired = Build.VERSION_CODES.S
        )
    ),
    Pair(
        Manifest.permission.BLUETOOTH_ADVERTISE,
        AndroidPermission(
            name = Manifest.permission.BLUETOOTH_ADVERTISE,
            minimumApiRequired = Build.VERSION_CODES.S
        )
    ),
    Pair(
        Manifest.permission.ACCESS_FINE_LOCATION,
        AndroidPermission(
            name = Manifest.permission.ACCESS_FINE_LOCATION,
        )
    ),
)