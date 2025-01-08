package com.cadrikmdev.bluetoothintercom

import android.app.Application
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin
import timber.log.Timber

class BluetoothIntercomApplication : Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    override fun onCreate() {

        super.onCreate()
        if (com.cadrikmdev.bluetoothintercom.BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger()
            androidContext(this@BluetoothIntercomApplication)
            workManagerFactory()
            modules(
//                appModule,
//                intercomDataDataModule,
//                permissionsModule,
            )
        }
    }
}