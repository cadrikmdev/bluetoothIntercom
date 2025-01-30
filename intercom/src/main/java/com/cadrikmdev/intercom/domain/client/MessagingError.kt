package com.cadrikmdev.intercom.domain.client

import com.cadrikmdev.intercom.domain.util.Error

enum class MessagingError : Error {
    CONNECTION_INTERRUPTED,
    DISCONNECTED,
    UNKNOWN
}