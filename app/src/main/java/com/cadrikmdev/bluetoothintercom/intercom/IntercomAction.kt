package com.cadrikmdev.bluetoothintercom.intercom

import kotlinx.serialization.Serializable

@Serializable
sealed interface IntercomAction {
    @Serializable
    data class Start(val address: String) : IntercomAction

    @Serializable
    data class Stop(val address: String) : IntercomAction
}