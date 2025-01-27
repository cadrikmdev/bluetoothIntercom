package com.cadrikmdev.bluetoothintercom.intercom

import com.cadrikmdev.intercom.domain.BluetoothServiceSpecification

class AppBluetoothServiceSpecification: BluetoothServiceSpecification {

    override fun getBluetoothServiceName(): String {
        return "IntercomTestAppService"
    }
}