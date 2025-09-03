package com.ledvance.tuya.command

import com.ledvance.tuya.command.dps.DeviceDp

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/4 14:14
 * Describe : DeviceCommand
 */
internal data class DeviceCommand(val key: String, val value: Any)

internal fun DeviceDp.command(value: Any, useCode: Boolean = true): DeviceCommand {
    return if (useCode) {
        DeviceCommand(code, value)
    } else {
        DeviceCommand(dpId.toString(), value)
    }
}