package com.cadrikmdev.intercom.domain.server

interface BluetoothAdvertiser {
    fun startAdvertising()

    fun stopAdvertising()
}