package com.cadrikmdev.intercom.data.client.mappers

import android.bluetooth.BluetoothDevice
import com.cadrikmdev.intercom.domain.client.DeviceNode
import com.cadrikmdev.intercom.domain.client.TrackingDevice
import com.cadrikmdev.intercom.domain.data.MeasurementState
import timber.log.Timber

fun BluetoothDevice.toDeviceNode(): DeviceNode? {
    return try {
        DeviceNode(
            address = this.address,
            displayName = this.name,
        )
    } catch (e: SecurityException) {
        e.printStackTrace()
        Timber.e(e.message)
        null
    }
}

fun BluetoothDevice.toTrackingDevice(): TrackingDevice? {
    return try {
        TrackingDevice(
            name = this.name,
            address = this.address,
            connected = false,
//            status = MeasurementState.UNKNOWN,
//            deviceAppVersion = "",
            updateTimestamp = System.currentTimeMillis()
        )
    } catch (e: SecurityException) {
        e.printStackTrace()
        Timber.e(e.message)
        null
    }
}

fun BluetoothDevice.toBluetoothDevice(): com.cadrikmdev.intercom.domain.data.BluetoothDevice? {
    return try {
        com.cadrikmdev.intercom.domain.data.BluetoothDevice(
            name = this.name,
            address = this.address,
        )
    } catch (e: SecurityException) {
        e.printStackTrace()
        Timber.e(e.message)
        null
    }
}

fun com.cadrikmdev.intercom.domain.data.BluetoothDevice.toTrackingDevice(): TrackingDevice? {
    return try {
        TrackingDevice(
            name = this.name,
            address = this.address,
            connected = false,
//            status = MeasurementState.UNKNOWN,
//            deviceAppVersion = "",
            updateTimestamp = System.currentTimeMillis()
        )
    } catch (e: SecurityException) {
        e.printStackTrace()
        Timber.e(e.message)
        null
    }
}