package com.cadrikmdev.bluetoothintercom.screens.home

sealed interface HomeScreenEvent {
    data object OnResumed: HomeScreenEvent
}