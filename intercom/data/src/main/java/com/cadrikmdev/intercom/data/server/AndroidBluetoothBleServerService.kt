package com.cadrikmdev.intercom.data.server

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattServer
import android.bluetooth.BluetoothGattServerCallback
import android.bluetooth.BluetoothGattService
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothProfile
import android.content.Context
import com.cadrikmdev.intercom.data.client.mappers.toBluetoothDevice
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import java.util.UUID

class AndroidBluetoothBleServerService(
    private val context: Context,
    private val messageProcessor: MessageProcessor,
    private val bluetoothServiceSpecification: BluetoothServiceSpecification
) : BluetoothServerService {

    private val serviceUUID: UUID = bluetoothServiceSpecification.getServiceUUID()
    private val characteristicUUID: UUID = bluetoothServiceSpecification.getServiceCharacteristicUUID()

    private val bluetoothManager: BluetoothManager by lazy {
        context.getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
    }
    private val bluetoothAdapter: BluetoothAdapter? by lazy { bluetoothManager.adapter }
    private var gattServer: BluetoothGattServer? = null

    private val connectedDevices = mutableSetOf<String>()

    private val _connectionStateFlow = MutableSharedFlow<ConnectionState>()
    override val connectionStateFlow: SharedFlow<ConnectionState> get() = _connectionStateFlow

    var getStatusUpdate: () -> MessageContent<SerializableContent>? = {
        MessageContent(
            content = TextContent("Simple text content example from BLE server side"),
            timestamp = System.currentTimeMillis()
        )
    }

    override fun setMeasurementProgressCallback(statusUpdate: () -> MessageContent<SerializableContent>?) {
        this.getStatusUpdate = statusUpdate
    }

    override fun startServer() {
        if (!context.isBluetoothConnectPermissionGranted() || bluetoothAdapter?.isEnabled != true) {
            Timber.e("Bluetooth not enabled or permissions not granted")
            return
        }

        gattServer = bluetoothManager.openGattServer(context, gattServerCallback)?.apply {
            addService(createGattService())
        }

        Timber.d("BLE GATT server started with service UUID: $serviceUUID")
    }

    private fun createGattService(): BluetoothGattService {
        // Create a GATT service with a single characteristic
        val service = BluetoothGattService(serviceUUID, BluetoothGattService.SERVICE_TYPE_PRIMARY)

        val characteristic = android.bluetooth.BluetoothGattCharacteristic(
            characteristicUUID,
            android.bluetooth.BluetoothGattCharacteristic.PROPERTY_READ or
                    android.bluetooth.BluetoothGattCharacteristic.PROPERTY_WRITE or
                    android.bluetooth.BluetoothGattCharacteristic.PROPERTY_NOTIFY,
            android.bluetooth.BluetoothGattCharacteristic.PERMISSION_READ or
                    android.bluetooth.BluetoothGattCharacteristic.PERMISSION_WRITE
        )

        service.addCharacteristic(characteristic)
        return service
    }

    private val gattServerCallback = object : BluetoothGattServerCallback() {
        override fun onConnectionStateChange(
            device: android.bluetooth.BluetoothDevice,
            status: Int,
            newState: Int
        ) {
            if (newState == BluetoothProfile.STATE_CONNECTED) {
                Timber.d("Device connected: ${device.address}")
                connectedDevices.add(device.address)
            } else if (newState == BluetoothProfile.STATE_DISCONNECTED) {
                Timber.d("Device disconnected: ${device.address}")
                connectedDevices.remove(device.address)
            }
        }

        override fun onCharacteristicReadRequest(
            device: android.bluetooth.BluetoothDevice,
            requestId: Int,
            offset: Int,
            characteristic: android.bluetooth.BluetoothGattCharacteristic
        ) {
            if (characteristic.uuid == characteristicUUID) {
//                val progress = getStatusUpdate()?.state?.name?.toByteArray() ?: byteArrayOf()
//                gattServer?.sendResponse(
//                    device,
//                    requestId,
//                    BluetoothGatt.GATT_SUCCESS,
//                    offset,
//                    progress
//                )
//                Timber.d("Read request from ${device.address}: ${String(progress)}")
            }
        }

        override fun onCharacteristicWriteRequest(
            device: android.bluetooth.BluetoothDevice,
            requestId: Int,
            characteristic: android.bluetooth.BluetoothGattCharacteristic,
            preparedWrite: Boolean,
            responseNeeded: Boolean,
            offset: Int,
            value: ByteArray
        ) {
            if (characteristic.uuid == characteristicUUID) {
                val message = String(value)
                Timber.d("Write request from ${device.address}: $message")

                CoroutineScope(Dispatchers.IO).launch {
                    val bluetoothDevice = device.toBluetoothDevice()
                    bluetoothDevice?.let {bluetoothDevice ->
                        messageProcessor.processMessageFrom(bluetoothDevice, message)
                    }
                }

                if (responseNeeded) {
                    gattServer?.sendResponse(
                        device,
                        requestId,
                        BluetoothGatt.GATT_SUCCESS,
                        offset,
                        null
                    )
                }
            }
        }

        override fun onNotificationSent(device: android.bluetooth.BluetoothDevice?, status: Int) {
            Timber.d("Notification sent to ${device?.address}: Status $status")
        }
    }


    override fun stopServer() {
        if (context.isBluetoothConnectPermissionGranted()) {
            gattServer?.close()
        }
        gattServer = null
        connectedDevices.clear()
        Timber.d("BLE GATT server stopped")
    }

}
