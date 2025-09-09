package com.ledvance.tuya.apis.domain

import android.content.Context
import com.thingclips.smart.sdk.bean.DeviceNodeBean

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/9 10:07
 * Describe : ITuyaMatterApi
 */
interface ITuyaMatterApi {
    suspend fun initFabric(context: Context, homeId: Long): Long?
    suspend fun initDevicesFabricNodes(homeId: Long): List<DeviceNodeBean>?
}