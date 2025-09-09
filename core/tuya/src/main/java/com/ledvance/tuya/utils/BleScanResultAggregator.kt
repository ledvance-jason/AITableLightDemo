package com.ledvance.tuya.utils

import com.ledvance.tuya.beans.ScanDevice
import com.ledvance.tuya.beans.isNameAndIconEmpty
import com.ledvance.tuya.beans.isTuyaDevice

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/10 14:58
 * Describe : BleScanResultAggregator
 */
class BleScanResultAggregator {
    private val devices = mutableMapOf<String, ScanDevice>()
    suspend fun aggregateDevices(
        scanItem: ScanDevice?,
        getNameAndIcon: suspend (ScanDevice) -> Pair<String, String>
    ): List<ScanDevice> {
        scanItem ?: return devices.filter { it.value.name.isNotEmpty() }.map { it.value }
        val oldDevice = devices[scanItem.address]
        val device = oldDevice?.copy(rssi = scanItem.rssi) ?: scanItem
        devices[scanItem.address] = if (device.isTuyaDevice() && device.isNameAndIconEmpty()) {
            val (name, icon) = getNameAndIcon(device)
            device.copy(name = name, icon = icon.ifEmpty { "unknown" })
        } else device
        return devices.filter { it.value.name.isNotEmpty() }.map { it.value }
    }

    fun reset() {
        devices.clear()
    }
}