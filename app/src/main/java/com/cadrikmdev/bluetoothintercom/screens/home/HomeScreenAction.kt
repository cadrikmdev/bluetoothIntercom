package com.cadrikmdev.bluetoothintercom.screens.home

sealed interface HomeScreenAction {
    data object SomeAction: HomeScreenAction
}