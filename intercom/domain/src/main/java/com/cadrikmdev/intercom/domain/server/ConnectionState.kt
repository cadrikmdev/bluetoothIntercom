package com.cadrikmdev.intercom.domain.server

enum class ConnectionState {
    STOPPED,
    STARTED,
    DISCONNECTED,
    WAITING_FOR_CONNECTION,
    CONNECTED,
    ERROR_STARTING,
    ERROR_CONNECTING,
    ERROR_RECEIVING,
    ERROR_SENDING,
    ERROR_COMMUNICATION,
    ERROR_CLOSING
}