package com.cadrikmdev.intercom.domain

import java.util.UUID

interface BluetoothServiceSpecification {

    fun getBluetoothServiceName(): String

    fun getServiceUUID(): UUID {
        return UUID.nameUUIDFromBytes(getBluetoothServiceName().toByteArray())
    }

    fun getServiceCharacteristicUUID(): UUID {
        return UUID.nameUUIDFromBytes("${getBluetoothServiceName()}Characteristic".toByteArray())
    }
}