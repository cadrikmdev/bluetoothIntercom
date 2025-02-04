package com.cadrikmdev.intercom.data.client.mappers

import android.bluetooth.BluetoothDevice
import timber.log.Timber

fun BluetoothDevice.toBluetoothDevice(): com.cadrikmdev.intercom.domain.data.BluetoothDevice? {
    return try {
        com.cadrikmdev.intercom.domain.data.BluetoothDevice(
            displayName = this.name,
            address = this.address,
        )
    } catch (e: SecurityException) {
        e.printStackTrace()
        Timber.e(e.message)
        null
    }
}
