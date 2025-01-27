package com.cadrikmdev.bluetoothintercom.di

import AndroidBluetoothService
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import androidx.core.content.getSystemService
import com.cadrikmdev.bluetoothintercom.BluetoothIntercomApplication
import com.cadrikmdev.bluetoothintercom.screens.classic.client.BluetoothClassicClientScreenViewModel
import com.cadrikmdev.bluetoothintercom.screens.classic.server.BluetoothClassicServerScreenViewModel
import com.cadrikmdev.bluetoothintercom.screens.home.HomeScreenViewModel
import com.cadrikmdev.intercom.data.AndroidBluetoothDevicesProvider
import com.cadrikmdev.intercom.data.AndroidMessageProcessor
import com.cadrikmdev.intercom.data.di.DI_BLUETOOTH_CLIENT_SERVICE_CLASSIC
import com.cadrikmdev.intercom.data.di.DI_BLUETOOTH_SERVER_SERVICE_CLASSIC
import com.cadrikmdev.intercom.data.server.AndroidBluetoothAdvertiser
import com.cadrikmdev.intercom.domain.BluetoothDevicesProvider
import com.cadrikmdev.intercom.domain.BluetoothServiceSpecification
import com.cadrikmdev.intercom.domain.ManagerControlServiceProtocol
import com.cadrikmdev.intercom.domain.message.MessageProcessor
import com.cadrikmdev.intercom.domain.server.BluetoothAdvertiser
import com.cadrikmdev.intercom.domain.service.BluetoothService
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.module

val appModule = module {

    single<CoroutineScope> {
        (androidApplication() as BluetoothIntercomApplication).applicationScope
    }

    viewModelOf(::HomeScreenViewModel)

    viewModel {
//        val bluetoothServerService =
//            get<BluetoothServerService>(named(DI_BLUETOOTH_SERVER_SERVICE_CLASSIC))
        BluetoothClassicServerScreenViewModel(
            get(named(DI_BLUETOOTH_SERVER_SERVICE_CLASSIC)),
            get(),
            get(),
        )
    }

    viewModel {
        BluetoothClassicClientScreenViewModel(
            get(named(DI_BLUETOOTH_CLIENT_SERVICE_CLASSIC)),
            get(),
            get(),
            get(),
            get(),
        )
    }
}