package com.ledvance.tuya.data.repo

import com.ledvance.tuya.apis.TuyaAccountApi
import com.ledvance.tuya.apis.TuyaActivatorApi
import com.ledvance.tuya.apis.TuyaDeviceApi
import com.ledvance.tuya.apis.TuyaHomeApi
import com.ledvance.tuya.apis.TuyaLongLinkApi
import com.ledvance.tuya.apis.TuyaMatterApi
import com.ledvance.tuya.apis.TuyaScanDeviceApi
import com.ledvance.tuya.apis.domain.ITuyaAccountApi
import com.ledvance.tuya.apis.domain.ITuyaActivatorApi
import com.ledvance.tuya.apis.domain.ITuyaDeviceApi
import com.ledvance.tuya.apis.domain.ITuyaHomeApi
import com.ledvance.tuya.apis.domain.ITuyaLongLinkApi
import com.ledvance.tuya.apis.domain.ITuyaMatterApi
import com.ledvance.tuya.apis.domain.ITuyaScanDeviceApi
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 08:39
 * Describe : TuyaRepo
 */
@Singleton
internal class TuyaRepo @Inject constructor(
    private val tuyaAccountApi: TuyaAccountApi,
    private val tuyaActivatorApi: TuyaActivatorApi,
    private val tuyaDeviceApi: TuyaDeviceApi,
    private val tuyaHomeApi: TuyaHomeApi,
    private val tuyaLongLinkApi: TuyaLongLinkApi,
    private val tuyaMatterApi: TuyaMatterApi,
    private val tuyaScanDeviceApi: TuyaScanDeviceApi,
) : ITuyaRepo {
    override fun getAccountApi(): ITuyaAccountApi {
        return tuyaAccountApi
    }

    override fun getActivatorApi(): ITuyaActivatorApi {
        return tuyaActivatorApi
    }

    override fun getDeviceApi(): ITuyaDeviceApi {
        return tuyaDeviceApi
    }

    override fun getHomeApi(): ITuyaHomeApi {
        return tuyaHomeApi
    }

    override fun getLongLinkApi(): ITuyaLongLinkApi {
        return tuyaLongLinkApi
    }

    override fun getMatterApi(): ITuyaMatterApi {
        return tuyaMatterApi
    }

    override fun getScanDeviceApi(): ITuyaScanDeviceApi {
        return tuyaScanDeviceApi
    }
}