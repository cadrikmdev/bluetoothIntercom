package com.cadrikmdev.intercom.domain.service

interface BluetoothService {
    fun isBluetoothEnabled(): Boolean

    fun openBluetoothSettings()
}