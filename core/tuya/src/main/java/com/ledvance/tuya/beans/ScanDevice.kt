package com.ledvance.tuya.beans

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/9 16:55
 * Describe : ScanDevice
 */
data class ScanDevice(
    val uuid: String,
    val pid: String,
    val name: String,
    val vendor: DeviceVendor,
    val networkType: DeviceNetworkType,
    val address: String = "",
    val mac: String = "",
    val icon: String = "",
    val deviceType: Int? = null,
    val extraObjectJson: String? = null,
    val rssi: Int = Int.MAX_VALUE,
)

fun ScanDevice.isTuyaDevice() = vendor == DeviceVendor.Tuya
fun ScanDevice.isNameAndIconEmpty() = name.isEmpty() && icon.isEmpty()
