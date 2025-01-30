package com.cadrikmdev.intercom.domain

import java.util.UUID

class ManagerControlServiceProtocol: com.cadrikmdev.intercom.domain.BluetoothServiceSpecification {

    override fun getBluetoothServiceName(): String {
        return "TrackerManagerControlServiceProtocol"
    }

    override fun getServiceCharacteristicUUID(): UUID {
        return UUID.nameUUIDFromBytes("TrackerManagerControlServiceCharacteristicProtocol".toByteArray())
    }

}