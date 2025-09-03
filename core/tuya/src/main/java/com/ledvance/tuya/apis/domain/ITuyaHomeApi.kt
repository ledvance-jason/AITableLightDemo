package com.ledvance.tuya.apis.domain

import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.sdk.bean.DeviceBean
import kotlinx.coroutines.flow.Flow

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 08:40
 * Describe : ITuyaHomeApi
 */
interface ITuyaHomeApi {
    fun shiftCurrentHome(homeId: Long, homeName: String?)
    fun getCurrentHomeId(): Long
    suspend fun getHomeDetail(homeId: Long, preferCache: Boolean = false): HomeBean?
    suspend fun refreshCurrentHome()
    fun updateCurrentHomeDevices()
    suspend fun getHomeList(): List<HomeBean?>?

    fun getDeviceListFlow(): Flow<List<DeviceBean>>
}