package com.ledvance.tuya.beans

import com.thingclips.smart.android.ble.api.ScanType

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/9 16:26
 * Describe : TuyaScanDeviceType
 */
enum class TuyaScanDeviceType {
    SINGLE,
    SINGLE_QR,
    MESH,
    SIG_MESH,
    NORMAL,
    THING_BEACON,
    MATTER,
    CAMERA
}