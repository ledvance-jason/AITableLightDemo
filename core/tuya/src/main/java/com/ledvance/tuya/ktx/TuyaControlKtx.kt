package com.ledvance.tuya.ktx

import com.ledvance.utils.extensions.toJson
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.sdk.api.IResultCallback
import com.thingclips.smart.sdk.bean.DeviceBean
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import kotlin.coroutines.resume

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/3 15:11
 * Describe : TuyaControlKtx
 */
private const val TAG = "DeviceControl"
suspend fun DeviceBean.publishDps(vararg dpsCmd: Pair<String, Any>) = suspendCancellableCoroutine {
    ThingHomeSdk.newDeviceInstance(devId)
        .publishDps(mapOf(*dpsCmd).toJson(), object : IResultCallback {
            override fun onError(code: String?, error: String?) {
                Timber.tag(TAG).e("publishDps onError: code:$code, error:$error")
                it.takeIf { it.isActive }?.resume(false)
            }

            override fun onSuccess() {
                Timber.tag(TAG).i("publishDps onSuccess")
                it.takeIf { it.isActive }?.resume(true)
            }

        })
}

suspend fun DeviceBean.publishCommands(vararg dpsCmd: Pair<String, Any>) =
    suspendCancellableCoroutine {
        ThingHomeSdk.newDeviceInstance(devId)
            .publishCommands(mapOf(*dpsCmd), object : IResultCallback {
                override fun onError(code: String?, error: String?) {
                    Timber.tag(TAG).e("publishCommands onError: code:$code, error:$error")
                    it.takeIf { it.isActive }?.resume(false)
                }

                override fun onSuccess() {
                    Timber.tag(TAG).i("publishCommands onSuccess")
                    it.takeIf { it.isActive }?.resume(true)
                }

            })
    }