package com.cadrikmdev.intercom.data.di

import AndroidBluetoothService
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import androidx.core.content.getSystemService
import com.cadrikmdev.intercom.data.AndroidBluetoothDevicesProvider
import com.cadrikmdev.intercom.data.AndroidMessageProcessor
import com.cadrikmdev.intercom.data.client.AndroidBluetoothBleClientService
import com.cadrikmdev.intercom.data.client.AndroidBluetoothClientService
import com.cadrikmdev.intercom.data.server.AndroidBluetoothAdvertiser
import com.cadrikmdev.intercom.data.server.AndroidBluetoothBleServerService
import com.cadrikmdev.intercom.data.server.AndroidBluetoothServerService
import com.cadrikmdev.intercom.domain.BluetoothDevicesProvider
import com.cadrikmdev.intercom.domain.BluetoothServiceSpecification
import com.cadrikmdev.intercom.domain.ManagerControlServiceProtocol
import com.cadrikmdev.intercom.domain.client.BluetoothClientService
import com.cadrikmdev.intercom.domain.message.MessageProcessor
import com.cadrikmdev.intercom.domain.server.BluetoothAdvertiser
import com.cadrikmdev.intercom.domain.server.BluetoothServerService
import com.cadrikmdev.intercom.domain.service.BluetoothService
import org.koin.android.ext.koin.androidApplication
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.module


const val DI_BLUETOOTH_CLIENT_SERVICE_BLE = "bluetoothLowEnergyClientService"
const val DI_BLUETOOTH_CLIENT_SERVICE_CLASSIC = "bluetoothClientService"

const val DI_BLUETOOTH_SERVER_SERVICE_BLE = "bluetoothLowEnergyServerService"
const val DI_BLUETOOTH_SERVER_SERVICE_CLASSIC = "bluetoothServerService"

val intercomDataModule = module {
    single<BluetoothServerService>(named(DI_BLUETOOTH_SERVER_SERVICE_CLASSIC)) {
        AndroidBluetoothServerService(
            get(),
            get(),
            get(),
        )
    }

    single<BluetoothServerService>(named(DI_BLUETOOTH_SERVER_SERVICE_BLE)) {
        AndroidBluetoothBleServerService(
            get(),
            get(),
            get(),
        )
    }

    single<BluetoothClientService>(named(DI_BLUETOOTH_CLIENT_SERVICE_CLASSIC)) {
        AndroidBluetoothClientService(
            get(),
            get(),
            get(),
            get(),
            get(),
        )
    }

    single<BluetoothClientService>(named(DI_BLUETOOTH_CLIENT_SERVICE_BLE)) {
        AndroidBluetoothBleClientService(
            get(),
            get(),
            get()
        )
    }

    single { androidApplication().getSystemService<BluetoothManager>() }

    single { androidApplication().getSystemService<BluetoothManager>()?.adapter }

    single<MessageProcessor> {
        AndroidMessageProcessor(
            null
        )
    }

    singleOf(::AndroidBluetoothAdvertiser).bind<BluetoothAdvertiser>()

    singleOf(::AndroidBluetoothDevicesProvider).bind<BluetoothDevicesProvider<BluetoothDevice>>()
    singleOf(::AndroidBluetoothService).bind<BluetoothService>()
    singleOf(::ManagerControlServiceProtocol).bind<BluetoothServiceSpecification>()
}