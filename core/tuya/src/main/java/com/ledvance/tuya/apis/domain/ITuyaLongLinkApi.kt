package com.ledvance.tuya.apis.domain

import com.ledvance.tuya.beans.DeviceState
import com.ledvance.tuya.beans.TuyaHomeChangeState
import kotlinx.coroutines.flow.Flow

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 08:41
 * Describe : ITuyaLongLinkApi
 */
interface ITuyaLongLinkApi {
    suspend fun getHomeStatusFlow(homeId: Long): Flow<TuyaHomeChangeState>
    fun getDeviceStatusFlow(): Flow<TuyaHomeChangeState>
    fun getHomeDeviceStateFlow(): Flow<Map<String, DeviceState>>
    fun updateHomeDeviceState(devId: String, switch: Boolean? = null, online: Boolean? = null)
}