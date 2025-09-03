package com.ledvance.tuya.command

import com.ledvance.tuya.command.dps.DeviceDp
import com.ledvance.utils.extensions.tryCatchReturn
import org.json.JSONObject

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/4 14:13
 * Describe : DeviceCommandBuilder
 */
class DeviceCommandBuilder(private val useCode: Boolean = true) {
    private val commands = mutableListOf<DeviceCommand>()

    fun add(dp: DeviceDp, value: Any): DeviceCommandBuilder {
        commands += dp.command(value, useCode)
        return this
    }

    fun buildJson(): JSONObject? {
        return tryCatchReturn {
            val json = org.json.JSONObject()
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