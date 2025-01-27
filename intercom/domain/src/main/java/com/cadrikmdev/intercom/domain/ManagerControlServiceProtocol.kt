package com.cadrikmdev.intercom.domain

import java.util.UUID

class ManagerControlServiceProtocol: BluetoothServiceSpecification {

    override fun getBluetoothServiceName(): String {
        return "TrackerManagerControlServiceProtocol"
    }

    override fun getServiceCharacteristicUUID(): UUID {
        return UUID.nameUUIDFromBytes("TrackerManagerControlServiceCharacteristicProtocol".toByteArray())
    }

}