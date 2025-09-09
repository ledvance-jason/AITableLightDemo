package com.ledvance.tuya.command

import com.ledvance.tuya.command.dps.DeviceDp
import com.ledvance.tuya.ktx.getDeviceSwitchDp
import com.ledvance.tuya.ktx.isTuyaLinkDevice
import com.ledvance.utils.extensions.tryCatchReturn
import com.thingclips.smart.sdk.bean.DeviceBean
import org.json.JSONObject

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/4 14:13
 * Describe : DeviceCommandBuilder
 */
class DeviceCommandBuilder(private val device: DeviceBean) {
    private val commands = mutableListOf<DeviceCommand>()
    private val useCode = device.isTuyaLinkDevice()

    fun addDp(dp: DeviceDp, value: Any): DeviceCommandBuilder {
        commands += dp.command(value, useCode)
        return this
    }

    fun addSwitch(value: Boolean): DeviceCommandBuilder {
        val deviceSwitchDp = device.getDeviceSwitchDp()
        commands += deviceSwitchDp.command(value, useCode)
        return this
    }

    fun buildJson(): JSONObject? {
        return tryCatchReturn {
            val json = JSONObject()
            commands.forEach { cmd ->
                json.put(cmd.key, cmd.value)
            }
            json
        }
    }

    fun buildMap(): Map<String, Any> {
        return tryCatchReturn {
            val map = mutableMapOf<String, Any>()
            commands.forEach { cmd ->
                map.put(cmd.key, cmd.value)
            }
            map
        } ?: mapOf()
    }
}