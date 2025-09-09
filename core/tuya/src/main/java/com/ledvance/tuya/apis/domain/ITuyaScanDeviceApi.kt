package com.ledvance.tuya.apis.domain

import com.ledvance.tuya.beans.ScanDevice
import com.ledvance.tuya.beans.TuyaScanDeviceSetting
import com.ledvance.tuya.beans.TuyaScanDeviceType
import kotlinx.coroutines.flow.Flow

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/9 10:34
 * Describe : ITuyaScanDeviceApi
 */
interface ITuyaScanDeviceApi {
    fun getScanDevicesFlow(): Flow<List<ScanDevice>>
    fun startScanDevice(
        setting: TuyaScanDeviceSetting = TuyaScanDeviceSetting.Builder()
            .addScanType(TuyaScanDeviceType.MATTER)
            .addScanType(TuyaScanDeviceType.CAMERA)
            .addScanType(TuyaScanDeviceType.MESH)
            .addScanType(TuyaScanDeviceType.SINGLE)
            .addScanType(TuyaScanDeviceType.SIG_MESH)
            .addScanType(TuyaScanDeviceType.NORMAL)
            .build()
    )

    fun stopScanDevice()
}