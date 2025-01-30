package com.cadrikmdev.bluetoothintercom.intercom

import com.cadrikmdev.intercom.domain.BluetoothServiceSpecification

class AppBluetoothServiceSpecification:
    com.cadrikmdev.intercom.domain.BluetoothServiceSpecification {

    override fun getBluetoothServiceName(): String {
        return "IntercomTestAppService"
    }
}