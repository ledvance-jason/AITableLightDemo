package com.ledvance.utils.ble

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothGatt
import android.bluetooth.BluetoothGattCallback
import android.bluetooth.BluetoothGattCharacteristic
import android.bluetooth.BluetoothGattService
import android.content.Context
import androidx.annotation.RequiresPermission
import com.ledvance.utils.extensions.bluetoothManager
import com.ledvance.utils.extensions.tryCatch

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/12 09:56
 * Describe : ClientBleGatt
 */
class ClientBleGatt {
    private var bluetoothGatt: BluetoothGatt? = null
    private var bleServices: List<BluetoothGattService>? = null
    private var bleCharacteristic: BluetoothGattCharacteristic? = null

    private val gattCallback = object : BluetoothGattCallback() {
        override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
            super.onConnectionStateChange(gatt, status, newState)
            when (newState) {
                BluetoothGatt.STATE_CONNECTED -> {
                    bluetoothGatt = gatt
                    @SuppressLint("MissingPermission")
                    tryCatch { bluetoothGatt?.discoverServices() }
                }

                BluetoothGatt.STATE_DISCONNECTED -> {
                    @SuppressLint("MissingPermission")
                    tryCatch { gatt?.close() }
                    bluetoothGatt = null
                }
            }
        }

        override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
            super.onServicesDiscovered(gatt, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                bleServices = gatt?.services
            }
        }

        override fun onCharacteristicRead(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray,
            status: Int
        ) {
            super.onCharacteristicRead(gatt, characteristic, value, status)
            if (status == BluetoothGatt.GATT_SUCCESS) {
                bleCharacteristic = characteristic
            }
        }

        override fun onCharacteristicChanged(
            gatt: BluetoothGatt,
            characteristic: BluetoothGattCharacteristic,
            value: ByteArray
        ) {
            super.onCharacteristicChanged(gatt, characteristic, value)
            bleCharacteristic = characteristic
        }
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun connect(
        context: Context,
        macAddress: String,
    ): BluetoothGatt? {
        val bluetoothManager = context.bluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter
        val device = bluetoothAdapter.getRemoteDevice(macAddress)
        bluetoothGatt = device.connectGatt(
            context, false, gattCallback,
            BluetoothDevice.TRANSPORT_LE
        )
        return bluetoothGatt
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    fun disconnect() {
        bluetoothGatt?.disconnect()
        bluetoothGatt?.close()
        bluetoothGatt = null
    }

    @SuppressLint("MissingPermission")
    fun readCharacteristic(characteristic: BluetoothGattCharacteristic) {
        bluetoothGatt?.readCharacteristic(characteristic)
    }

    // 写入特征值（异步）
    @SuppressLint("MissingPermission")
    fun writeCharacteristic(
        characteristic: BluetoothGattCharacteristic,
        data: ByteArray
    ) {
        // 设置写入数据
        characteristic.value = data
        // 写入（需根据特征的属性选择写入类型）
        val writeType = when {
            characteristic.properties and BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE != 0 ->
                BluetoothGattCharacteristic.WRITE_TYPE_NO_RESPONSE

            else -> BluetoothGattCharacteristic.WRITE_TYPE_DEFAULT
        }
        characteristic.writeType = writeType
        bluetoothGatt?.writeCharacteristic(characteristic)
    }
}