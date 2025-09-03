package com.ledvance.tuya.beans

import com.ledvance.tuya.ktx.getSwitchState
import com.thingclips.smart.sdk.bean.DeviceBean

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/4 16:32
 * Describe : DeviceState
 */
data class DeviceState(
    val devId: String,
    val online: Boolean,
    val switch: Boolean
)

fun DeviceBean.toDeviceState() =
    DeviceState(devId = devId, online = isOnline, switch = getSwitchState())