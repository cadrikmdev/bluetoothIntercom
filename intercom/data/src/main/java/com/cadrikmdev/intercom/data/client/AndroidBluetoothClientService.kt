package com.cadrikmdev.intercom.data.client

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import com.cadrikmdev.intercom.data.util.isBluetoothConnectPermissionGranted
import com.cadrikmdev.intercom.data.util.isFineLocationPermissionGranted
import com.cadrikmdev.intercom.domain.BluetoothDevicesProvider
import com.cadrikmdev.intercom.domain.BluetoothServiceSpecification
import com.cadrikmdev.intercom.domain.client.BluetoothClientService
import com.cadrikmdev.intercom.domain.client.BluetoothError
import com.cadrikmdev.intercom.domain.client.DeviceType
import com.cadrikmdev.intercom.domain.message.MessageProcessor
import com.cadrikmdev.intercom.domain.message.MessageWrapper
import com.cadrikmdev.intercom.domain.util.Result
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import kotlinx.coroutines.supervisorScope
import timber.log.Timber
import java.io.IOException
import java.io.OutputStream

class AndroidBluetoothClientService(
    private val context: Context,
    private val devicesProvider: BluetoothDevicesProvider<BluetoothDevice>,
    private val bluetoothSpecificationProvider: BluetoothServiceSpecification,
    private val applicationScope: CoroutineScope,
    private val messageProcessor: MessageProcessor,
) : BluetoothClientService {

    override val receivedActionFlow: MutableStateFlow<MessageWrapper?> =
        MutableStateFlow<MessageWrapper?>(null)

    override val sendActionFlow: MutableStateFlow<MessageWrapper?> =
        MutableStateFlow<MessageWrapper?>(null)

    val sendActionJob: Job = sendActionFlow.onEach {

    }.launchIn(
        applicationScope
    )

    override val pairedBluetoothDevices = MutableStateFlow<Map<String, com.cadrikmdev.intercom.domain.data.BluetoothDevice>>(mapOf())

    private var _pairedDevices = MutableStateFlow<Set<BluetoothDevice>>(setOf())
    val pairedDevices = _pairedDevices.asStateFlow()

    private var _connections = MutableStateFlow<Map<String, BluetoothSocket>>(mapOf())
    val connections = _connections.asStateFlow()

    private var _outputStream = MutableStateFlow<Map<String, OutputStream>>(mapOf())
    val outputStream = _outputStream.asStateFlow()

    private var bluetoothAdapter: BluetoothAdapter? = null

    init {
        devicesProvider.observePairedDevices(DeviceType.WORKER)
//        .onEach { devices ->
//            val trackDevices = devices.values
//                .mapNotNull {
//                    it.toTrackingDevice()
//                }
//                .associateBy { it.address }
//                .toMap()
//
//            trackingDevices.emit(trackDevices)
//        }
            .launchIn(applicationScope + Dispatchers.IO)

        devicesProvider.pairedDevices.onEach { devices ->
            val trackDevices = devices.values
                .associateBy { it.address }
                .toMap()

            pairedBluetoothDevices.emit(trackDevices)
        }.launchIn(applicationScope + Dispatchers.IO)

        sendActionFlow.onEach { message ->
            // TODO: Inject some message processor
            val actionDeviceAddress = message?.destinationAddress

            _connections.value.forEach { deviceAddress, socket ->
                if (deviceAddress == actionDeviceAddress && socket.isConnected) {
                    Timber.d("Establishing output stream to $deviceAddress")
                    CoroutineScope(Dispatchers.IO).launch {
                        val outputStream = _outputStream.value[deviceAddress] ?: socket.outputStream
                        _outputStream.value.plus(Pair(deviceAddress, outputStream))
                        Timber.d("Trying to send message: $message")
                        try {
                            // Using supervisorScope to ensure child coroutines are handled properly
                            supervisorScope {
                                val sendJob = launch {
                                    try {
                                        if (isActive) { // Check if the coroutine is still active
                                            val message =
                                                messageProcessor.sendAction(sendActionFlow.value)
                                            Timber.d("Sending: $message")
                                            message?.let {
                                                outputStream.write((message).toByteArray())
                                                outputStream.flush()
                                            }
                                            sendActionFlow.value = null
                                        }
                                    } catch (e: IOException) {
                                        Timber.e(e, "Error occurred during sending data")
                                    }
                                }

                                // Wait for both jobs to complete, or for cancellation
                                sendJob.join()
                            }
                        } catch (e: IOException) {
                            Timber.e(e, "Error occurred during communication")
                        } finally {
                            // Ensure the streams and socket are closed
                            try {
//                                outputStream.close()
//                                _outputStream.value = removeKeyFromMap(_outputStream.value, name)
                            } catch (e: IOException) {
                                Timber.e(e, "Error occurred while closing the socket")
                            }
                        }
                    }

                }

            }
        }.launchIn(applicationScope)
    }

    override suspend fun connectToDevice(deviceAddress: String): Result<Boolean, BluetoothError> {
        // Ensure the location permission is granted (required for Bluetooth discovery from Android M+)
        if (!context.isFineLocationPermissionGranted()) return Result.Error(BluetoothError.NO_FINE_LOCATION_PERMISSIONS)
        if (!context.isBluetoothConnectPermissionGranted()) {
            return Result.Error(BluetoothError.MISSING_BLUETOOTH_CONNECT_PERMISSION)
        }

        val bluetoothDevice =
            devicesProvider.getNativeBluetoothDeviceFromDeviceAddress(deviceAddress)
            ?: return Result.Error(BluetoothError.BLUETOOTH_DEVICE_NOT_FOUND)

        // Use CompletableDeferred to wait for the result
        val resultDeferred = CompletableDeferred<Result<Boolean, BluetoothError>>()

        Timber.d("Connecting to device with address: ${deviceAddress}")
        Timber.d("Service uuid: ${bluetoothSpecificationProvider.getServiceUUID()}")
        Timber.d("Connecting to device with supportedServices; ${bluetoothDevice.uuids.forEach { "${it.uuid}, " }}")
        bluetoothDevice.createRfcommSocketToServiceRecord(bluetoothSpecificationProvider.getServiceUUID())
        bluetoothDevice?.let { device ->
            val clientSocket: BluetoothSocket =
                device.createRfcommSocketToServiceRecord(
                    bluetoothSpecificationProvider.getServiceUUID()
                )
            // Accept connections from clients (running in a separate thread)
            Thread {
                clientSocket?.let { socket ->
                    // Connect to the remote device through the socket. This call blocks
                    // until it succeeds or throws an exception.
                    try {
                        socket.connect()
                        Timber.d("Connected to server socket successfully on ${bluetoothDevice.address}")
                    } catch (e: IOException) {
                        Timber.e("Unable to connect to server socket ${e.printStackTrace()}")
                    }

                    // The connection attempt succeeded. Perform work associated with
                    // the connection in a separate thread.
                    manageConnectedSocket(socket)
                }

            }.start()
        }
        return Result.Success(true)
    }

    override fun disconnectFromDevice(deviceAddress: String): Boolean {
        try {
            markDeviceDisconnected(deviceAddress)
            if (_connections.value[deviceAddress] == null) {
                return true
            }
            _connections.value[deviceAddress]?.close()
            _connections.value = removeKeyFromMap(_connections.value, deviceAddress)
            return true
        } catch (e: IOException) {
            Timber.e(e, "Error occurred while closing the socket")
            return false
        }

    }

    private fun <T> removeKeyFromMap(map: Map<String, T>, key: String) : Map<String, T> {
        return map.filter { it.key != key }
    }


    private fun manageConnectedSocket(socket: BluetoothSocket) {
        // Implement logic for communication with the connected server
        _connections.value = _connections.value.plus(Pair(socket.remoteDevice.address, socket))
        val connectedDevice = pairedBluetoothDevices.value[socket.remoteDevice.address]?.copy(
            connected = true
        )
        connectedDevice?.let {
            val tmpTrackingDevices = pairedBluetoothDevices.value.toMutableMap()
            tmpTrackingDevices[socket.remoteDevice.address] = it
            // Coroutine for receiving data
            CoroutineScope(Dispatchers.IO).launch {
                pairedBluetoothDevices.emit(tmpTrackingDevices)
            }
        }

        // Launch a coroutine for receiving data

        CoroutineScope(Dispatchers.IO).launch {
            val inputStream = socket.inputStream
            val outputStream = socket.outputStream
            val reader = inputStream.bufferedReader()
            val address = socket.remoteDevice.address
            val name = socket.remoteDevice.name
            try {
                // Using supervisorScope to ensure child coroutines are handled properly
                supervisorScope {
                    // Coroutine for receiving data
                    val receiveJob = launch {
                        try {
                            while (isActive) { // Check if the coroutine is still active
                                val message = reader.readLine() ?: break
                                Timber.d("Received from server: $message")
                                // Handle the received message
                                val sourceDevice = com.cadrikmdev.intercom.domain.data.BluetoothDevice(
                                    displayName = name,
                                    address = address,
                                    connected = true,
                                    lastUpdatedTimestamp = System.currentTimeMillis()
                                )
                                val processedMessage = messageProcessor.processMessageFrom(sourceDevice, message)
                                receivedActionFlow.emit(processedMessage)
                            }
                        } catch (e: IOException) {
                            Timber.e(e, "Error occurred during receiving data")
                        } finally {
                            markDeviceDisconnected(address)
                        }

                    }

                    // Coroutine for sending data
                    val sendJob = launch {
                        try {
                            while (isActive) { // Check if the coroutine is still active
                                sendActionJob.join()
                                val message = messageProcessor.sendAction(sendActionFlow.value)
                                Timber.d("Sending: $message")
                                message?.let {
                                    outputStream.write((message).toByteArray())
                                    outputStream.flush()
                                }
                            }
                        } catch (e: IOException) {
                            Timber.e(e, "Error occurred during sending data")
                        } finally {
                            markDeviceDisconnected(address)
                        }
                    }

                    // Wait for both jobs to complete, or for cancellation
                    receiveJob.join()
                    sendJob.join()
                }
            } catch (e: IOException) {
                Timber.e(e, "Error occurred during communication")
            } finally {
                // Ensure the streams and socket are closed
                try {
                    markDeviceDisconnected(address)
                    reader.close()
                    outputStream.close()
                    socket.close()
                    Timber.d("Socket closed: ${socket.remoteDevice.address}")
                } catch (e: IOException) {
                    Timber.e(e, "Error occurred while closing the socket")
                }
            }
        }
    }

    private fun updateStatus(address: String, sendMessage: MessageWrapper.SendMessage) {
        val updatedDevice = pairedBluetoothDevices.value[address]?.copy(
//            status = updateProgress.progress.state,
            lastUpdatedTimestamp = sendMessage.content.timestamp,
//            deviceAppVersion = updateProgress.progress.appVersion ?: "",
        )
        updatedDevice?.let {
            val tmpTrackingDevices = pairedBluetoothDevices.value.toMutableMap()
            tmpTrackingDevices[address] = it
            // Coroutine for receiving data
            CoroutineScope(Dispatchers.IO).launch {
                pairedBluetoothDevices.emit(tmpTrackingDevices)
            }
        }
    }

    private fun markDeviceDisconnected(address: String) {
        val connectedDevice = pairedBluetoothDevices.value[address]?.copy(
            connected = false,
//            status = MeasurementState.UNKNOWN,
            lastUpdatedTimestamp = System.currentTimeMillis()
        )
        connectedDevice?.let {
            val tmpTrackingDevices = pairedBluetoothDevices.value.toMutableMap()
            tmpTrackingDevices[address] = it
            // Coroutine for receiving data
            CoroutineScope(Dispatchers.IO).launch {
                pairedBluetoothDevices.emit(tmpTrackingDevices)
            }
        }
    }

    private fun getBluetoothDeviceFromDeviceAddress(deviceAddress: String): BluetoothDevice? {
        return try {
            _pairedDevices.value.first { it.address == deviceAddress }
        } catch (e: NoSuchElementException) {
            e.printStackTrace()
            null
        }
    }
}