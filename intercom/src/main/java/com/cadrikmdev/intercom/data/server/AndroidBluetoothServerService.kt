package com.cadrikmdev.intercom.data.server

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.Context
import com.cadrikmdev.intercom.data.util.isBluetoothConnectPermissionGranted
import com.cadrikmdev.intercom.domain.BluetoothServiceSpecification
import com.cadrikmdev.intercom.domain.data.MessageContent
import com.cadrikmdev.intercom.domain.data.TextContent
import com.cadrikmdev.intercom.domain.message.MessageProcessor
import com.cadrikmdev.intercom.domain.message.MessageWrapper
import com.cadrikmdev.intercom.domain.message.SerializableContent
import com.cadrikmdev.intercom.domain.server.BluetoothServerService
import com.cadrikmdev.intercom.domain.server.ConnectionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import timber.log.Timber
import java.io.IOException
import java.util.UUID

class AndroidBluetoothServerService(
    private val context: Context,
    private val messageProcessor: MessageProcessor,
    private val bluetoothServiceSpecification: com.cadrikmdev.intercom.domain.BluetoothServiceSpecification
) : BluetoothServerService {

    private val serviceUUID: UUID = bluetoothServiceSpecification.getServiceUUID()

    private var bluetoothServerSocket: BluetoothServerSocket? = null
    private var bluetoothSocket: BluetoothSocket? = null
    private var gattServer: BluetoothGattServer? = null
    private var bluetoothAdapter: BluetoothAdapter? = null

    var getStatusUpdate: () -> MessageContent<SerializableContent>? = {
        MessageContent(
            content = TextContent("Simple data from server side"),
            timestamp = System.currentTimeMillis()
        )
    }

    init {
        CoroutineScope(Dispatchers.IO).launch {
            _connectionStateFlow.emit(ConnectionState.STOPPED)
        }
    }

    private val _connectionStateFlow = MutableStateFlow<ConnectionState>(ConnectionState.STOPPED)
    override val connectionStateFlow: StateFlow<ConnectionState> get() = _connectionStateFlow

    override fun setMeasurementProgressCallback(statusUpdate: () -> MessageContent<SerializableContent>?) {
        this.getStatusUpdate = statusUpdate
    }

    override fun startServer() {
        val bluetoothManager =
            context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter

        // Ensure Bluetooth is supported and enabled on the device
        if (!context.isBluetoothConnectPermissionGranted() || bluetoothAdapter == null || bluetoothAdapter?.isEnabled != true) {
            // Bluetooth is not supported or not enabled
            CoroutineScope(Dispatchers.IO).launch {
                _connectionStateFlow.emit(ConnectionState.ERROR_STARTING)
            }
            return
        }

        Timber.d("starting listening for connections with service uuid = ${serviceUUID}")
        // You can set up Bluetooth classic (RFCOMM) or use BLE advertising for discovery
        // For RFCOMM, you would use something like the following:
        bluetoothAdapter?.let {
            if (bluetoothSocket?.isConnected == true) {
                Timber.d("Bluetooth socket is already created.")
                CoroutineScope(Dispatchers.IO).launch {
                    _connectionStateFlow.emit(ConnectionState.CONNECTED)
                }
                return
            }
            val serverSocket: BluetoothServerSocket? =
                it.listenUsingRfcommWithServiceRecord("MyService", serviceUUID)
            // Accept connections from clients (running in a separate thread)
            Timber.d("starting listenning for connections with service uuid = ${serviceUUID}")
            Thread {
                var shouldLoop = true
                CoroutineScope(Dispatchers.IO).launch {
                    Timber.d("Waiting for client to connect")
                    _connectionStateFlow.emit(ConnectionState.WAITING_FOR_CONNECTION)
                }
                while (shouldLoop) {
                    try {
                        Timber.d("Trying to establish connection with client")
                        val socket: BluetoothSocket? = serverSocket?.accept()
                        socket?.let {
                            // Handle the connection in a separate thread
                            Timber.d("Connection made successfully with client")
                            CoroutineScope(Dispatchers.IO).launch {
                                _connectionStateFlow.emit(ConnectionState.CONNECTED)
                            }
                            manageConnectedSocket(it)
                        }
                    } catch (e: IOException) {
                        Timber.e("Socket's accept() method failed", e)
                        Thread.sleep(1000L)
                        CoroutineScope(Dispatchers.IO).launch {
                            _connectionStateFlow.emit(ConnectionState.ERROR_CONNECTING)
                        }
                        shouldLoop = true // todo change to false
                    }
                }
            }.start()
        }
    }

    private fun manageConnectedSocket(socket: BluetoothSocket) {
        // Implement logic for communication with the connected client
        bluetoothSocket = socket

        CoroutineScope(Dispatchers.IO).launch {
            val inputStream = socket.inputStream
            val outputStream = socket.outputStream
            val reader = inputStream.bufferedReader()
            val address = socket.remoteDevice.address

            try {
                // Using supervisorScope to manage child coroutines
                supervisorScope {
                    // Coroutine for receiving data
                    val receiveJob = launch {
                        try {
                            while (isActive) {
                                val message = reader.readLine() ?: break
                                Timber.d("Received: $message")
                                // Handle the received message
                                val action = messageProcessor.processMessageFrom(address, message)

                            }
                        } catch (e: IOException) {
                            Timber.e(e, "Error occurred during receiving data")
                            CoroutineScope(Dispatchers.IO).launch {
                                _connectionStateFlow.emit(ConnectionState.ERROR_RECEIVING)
                            }
                        }
                    }

                    // Coroutine for sending data
                    val sendJob = launch {
                        try {
                            while (isActive) {
                                val message = getStatusUpdate()
                                message?.let {
                                    val encodedMessage = messageProcessor.sendAction(
                                        MessageWrapper.SendMessage(address = "", content = it)
                                    )
                                    Timber.d("Sending: $encodedMessage")
                                    val byteArray = encodedMessage?.toByteArray()
                                    byteArray?.let {
                                        outputStream.write(it)
                                        outputStream.flush()
                                    }
                                }
                                delay(1000) // Wait for 1 seconds before sending the next message
                            }
                        } catch (e: IOException) {
                            Timber.e(e, "Error occurred during sending data")
                            CoroutineScope(Dispatchers.IO).launch {
                                _connectionStateFlow.emit(ConnectionState.ERROR_SENDING)
                            }
                        }
                    }

                    // Await completion of both coroutines
                    receiveJob.join()
                    sendJob.join()
                }
            } catch (e: IOException) {
                Timber.e(e, "Error occurred during communication")
                CoroutineScope(Dispatchers.IO).launch {
                    _connectionStateFlow.emit(ConnectionState.ERROR_COMMUNICATION)
                }
            } finally {
                // Ensure the streams and socket are closed properly
                try {
                    reader.close()
                    outputStream.close()
                    socket.close()
                    Timber.d("Socket closed: ${socket.remoteDevice.address}")
                } catch (e: IOException) {
                    Timber.e(e, "Error occurred while closing the socket")
                    CoroutineScope(Dispatchers.IO).launch {
                        _connectionStateFlow.emit(ConnectionState.ERROR_CLOSING)
                    }
                }
                CoroutineScope(Dispatchers.IO).launch {
                    _connectionStateFlow.emit(ConnectionState.DISCONNECTED)
                }
            }
        }
    }

    override fun stopServer() {
        try {
            bluetoothSocket?.close()
            CoroutineScope(Dispatchers.IO).launch {
                _connectionStateFlow.emit(ConnectionState.DISCONNECTED)
            }
        } catch (e: IOException) {
            Timber.e(e, "Error occurred while closing the socket")
            CoroutineScope(Dispatchers.IO).launch {
                _connectionStateFlow.emit(ConnectionState.ERROR_CLOSING)
            }
        }

    }
}