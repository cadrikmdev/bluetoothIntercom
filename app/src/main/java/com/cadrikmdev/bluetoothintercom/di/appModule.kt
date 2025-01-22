package com.cadrikmdev.bluetoothintercom.di

import com.cadrikmdev.bluetoothintercom.BluetoothIntercomApplication
import com.cadrikmdev.bluetoothintercom.screens.classic.client.BluetoothClassicClientScreenViewModel
import com.cadrikmdev.bluetoothintercom.screens.classic.server.BluetoothClassicServerScreenViewModel
import com.cadrikmdev.bluetoothintercom.screens.home.HomeScreenViewModel
import com.cadrikmdev.intercom.data.di.DI_BLUETOOTH_CLIENT_SERVICE_CLASSIC
import com.cadrikmdev.intercom.data.di.DI_BLUETOOTH_SERVER_SERVICE_CLASSIC
import com.cadrikmdev.intercom.domain.client.BluetoothClientService
import com.cadrikmdev.intercom.domain.server.BluetoothServerService
import kotlinx.coroutines.CoroutineScope
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
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
        )
    }

    viewModel {
        BluetoothClassicClientScreenViewModel(
            get(named(DI_BLUETOOTH_CLIENT_SERVICE_CLASSIC)),
            get(),
            get(),
            get(),
        )
    }
}