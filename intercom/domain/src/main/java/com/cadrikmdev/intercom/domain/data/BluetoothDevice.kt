package com.cadrikmdev.intercom.domain.data

import kotlinx.serialization.Serializable

@Serializable
data class BluetoothDevice(
    val displayName: String,
    val address: String,
    val connected: Boolean = false,
    val lastUpdatedTimestamp: Long = System.currentTimeMillis(),
//    val connected: Boolean = false,
//    val lastUpdatedTimestamp: Long = System.currentTimeMillis(),
) {
    fun isTheSameDevice(otherDevice: BluetoothDevice): Boolean {
        return this.address == otherDevice.address
    }
}