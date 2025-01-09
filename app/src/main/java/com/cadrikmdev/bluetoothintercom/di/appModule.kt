package com.cadrikmdev.bluetoothintercom.di

import com.cadrikmdev.bluetoothintercom.screens.home.HomeScreenViewModel
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::HomeScreenViewModel)
}