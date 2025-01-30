package com.cadrikmdev.intercom.domain.client


data class TrackingDevice(
    val name: String,
    val address: String,
    val connected: Boolean,
    val updateTimestamp: Long,
) {
    fun isTheSameDevice(otherDevice: TrackingDevice): Boolean {
        return this.address == otherDevice.address
    }
}