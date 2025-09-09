package com.ledvance.tuya.apis.domain

import com.thingclips.smart.sdk.bean.DeviceBean
import org.json.JSONObject

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 08:41
 * Describe : ITuyaDeviceApi
 */
interface ITuyaDeviceApi {
    suspend fun publishDps(devId: String, command: JSONObject?): Result<Boolean>
    suspend fun deleteDevice(devId: String, isReset: Boolean): Boolean
    fun getDevice(devId: String): DeviceBean?
}