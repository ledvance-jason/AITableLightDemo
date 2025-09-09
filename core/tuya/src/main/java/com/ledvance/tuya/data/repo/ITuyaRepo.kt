package com.ledvance.tuya.data.repo

import com.ledvance.tuya.apis.domain.ITuyaAccountApi
import com.ledvance.tuya.apis.domain.ITuyaActivatorApi
import com.ledvance.tuya.apis.domain.ITuyaDeviceApi
import com.ledvance.tuya.apis.domain.ITuyaHomeApi
import com.ledvance.tuya.apis.domain.ITuyaLongLinkApi
import com.ledvance.tuya.apis.domain.ITuyaMatterApi
import com.ledvance.tuya.apis.domain.ITuyaScanDeviceApi

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 08:39
 * Describe : ITuyaRepo
 */
interface ITuyaRepo {
    fun getAccountApi(): ITuyaAccountApi
    fun getActivatorApi(): ITuyaActivatorApi
    fun getDeviceApi(): ITuyaDeviceApi
    fun getHomeApi(): ITuyaHomeApi
    fun getLongLinkApi(): ITuyaLongLinkApi
    fun getMatterApi(): ITuyaMatterApi
    fun getScanDeviceApi(): ITuyaScanDeviceApi
}