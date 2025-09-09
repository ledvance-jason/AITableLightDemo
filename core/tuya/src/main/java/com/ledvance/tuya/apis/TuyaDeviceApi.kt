package com.ledvance.tuya.apis

import com.ledvance.tuya.apis.domain.ITuyaDeviceApi
import com.ledvance.tuya.ktx.isTuyaLinkDevice
import com.ledvance.utils.extensions.toJson
import com.thingclips.sdk.os.ThingOSDevice
import com.thingclips.smart.android.device.enums.ThingSmartThingMessageType
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.sdk.api.IResultCallback
import com.thingclips.smart.sdk.api.IThingDataCallback
import com.thingclips.smart.sdk.bean.DeviceBean
import com.thingclips.smart.sdk.bean.ThingSmartThingModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import org.json.JSONObject
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/4 11:08
 * Describe : TuyaDeviceApi
 */
@Singleton
internal class TuyaDeviceApi @Inject constructor() : ITuyaDeviceApi {
    private val TAG = "TuyaDeviceApi"

    private suspend fun checkThingMode(devId: String): Boolean = withContext(Dispatchers.IO) {
        val deviceBean = ThingOSDevice.getDeviceBean(devId) ?: return@withContext false
        val isTuyaLinkDevice = deviceBean.isTuyaLinkDevice()
        Timber.tag(TAG).i("fetchThingMode: isTuyaLinkDevice:$isTuyaLinkDevice")
        if (!isTuyaLinkDevice) {
            return@withContext true
        }
        if (deviceBean.thingModel != null) {
            return@withContext true
        }
        val productId = deviceBean.getProductId()
        Timber.tag(TAG).i("fetchThingMode: devId:$devId, productId:$productId")
        return@withContext suspendCancellableCoroutine {
            ThingOSDevice.getDeviceOperator().getThingModelWithProductId(productId, object :
                IThingDataCallback<ThingSmartThingModel> {
                override fun onSuccess(result: ThingSmartThingModel?) {
                    Timber.tag(TAG).i("fetchThingMode onSuccess: ${result?.services.toJson()}")
                    it.takeIf { it.isActive }?.resume(true)
                }

                override fun onError(errorCode: String?, errorMessage: String?) {
                    Timber.tag(TAG).e("fetchThingMode onError: code:$errorCode, msg:$errorMessage")
                    it.takeIf { it.isActive }?.resume(false)
                }

            })
        }
    }

    override suspend fun publishDps(devId: String, command: JSONObject?): Result<Boolean> =
        withContext(Dispatchers.IO) {
            if (!checkThingMode(devId)) {
                return@withContext Result.failure(Throwable("thing mode fetch failed!"))
            }
            Timber.tag(TAG).i("publishDps($devId): $command")
            command ?: return@withContext Result.failure(Throwable("command is null!"))
            val device = ThingHomeSdk.getDataInstance().getDeviceBean(devId)
                ?: return@withContext Result.failure(Throwable("device fetch failed!"))
            return@withContext suspendCancellableCoroutine {
                val callback = object : IResultCallback {
                    override fun onError(code: String?, error: String?) {
                        Timber.tag(TAG).e("publishDps onError: code:$code, msg:$error")
                        val exception = Throwable(error ?: "publishDps failed!")
                        it.takeIf { it.isActive }?.resume(Result.failure(exception))
                    }

                    override fun onSuccess() {
                        Timber.tag(TAG).i("publishDps $command onSuccess")
                        it.takeIf { it.isActive }?.resume(Result.success(true))
                    }
                }
                when {
                    device.isTuyaLinkDevice() -> {
                        ThingHomeSdk.newDeviceInstance(devId).publishThingMessageWithType(
                            ThingSmartThingMessageType.PROPERTY,
                            command,
                            callback
                        )
                    }

                    else -> {
                        ThingHomeSdk.newDeviceInstance(devId).publishDps(
                            command.toString(),
                            callback
                        )
                    }
                }
            }
        }

    override suspend fun deleteDevice(devId: String, isReset: Boolean) =
        suspendCancellableCoroutine {
            Timber.tag(TAG).i("deleteDevice: devId->$devId")
            val thingDevice = ThingHomeSdk.newDeviceInstance(devId)
            val callback = object : IResultCallback {
                override fun onError(code: String?, error: String?) {
                    Timber.tag(TAG).e("deleteDevice(isReset:$isReset) code:$code, error:$error")
                    it.takeIf { it.isActive }?.resume(false)
                }

                override fun onSuccess() {
                    Timber.tag(TAG).i("deleteDevice(isReset:$isReset) onSuccess")
                    it.takeIf { it.isActive }?.resume(true)
                }
            }
            if (isReset) {
                thingDevice.resetFactory(callback)
            } else {
                thingDevice.removeDevice(callback)
            }
        }

    override fun getDevice(devId: String): DeviceBean? {
        return ThingHomeSdk.getDataInstance().getDeviceBean(devId)
    }
}