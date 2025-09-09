package com.ledvance.tuya.apis

import android.content.Context
import com.ledvance.tuya.apis.domain.ITuyaMatterApi
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.sdk.api.IThingDataCallback
import com.thingclips.smart.sdk.bean.DeviceNodeBean
import com.thingclips.smart.sdk.bean.OpenFabricInfo
import kotlinx.coroutines.suspendCancellableCoroutine
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/9 10:06
 * Describe : TuyaMatterApi
 */
@Singleton
class TuyaMatterApi @Inject constructor() : ITuyaMatterApi {
    private val TAG = "TuyaMatterApi"
    override suspend fun initFabric(context: Context, homeId: Long) = suspendCancellableCoroutine {
        Timber.tag(TAG).i("initFabric: homeId:$homeId")
        ThingHomeSdk.getFabricManager()
            .initFabric(context, homeId, object : IThingDataCallback<OpenFabricInfo> {
                override fun onSuccess(result: OpenFabricInfo?) {
                    Timber.tag(TAG).i("initFabric onSuccess: fabricId->${result?.fabricId}")
                    it.takeIf { it.isActive }?.resume(result?.fabricId)
                }

                override fun onError(errorCode: String?, errorMessage: String?) {
                    Timber.tag(TAG).e("initFabric onError: code:$errorCode,msg:$errorMessage")
                    it.takeIf { it.isActive }?.resume(null)
                }
            })
    }

    override suspend fun initDevicesFabricNodes(homeId: Long) = suspendCancellableCoroutine {
        Timber.tag(TAG).i("initDevicesFabricNodes: homeId:$homeId")
        val matterDevIds = ThingHomeSdk.getDataInstance().getHomeDeviceList(homeId)?.filter {
            it.isMatter
        }?.map { it.devId }?.toSet() ?: setOf()
        Timber.tag(TAG).i("initDevicesFabricNodes matterDevIds: $matterDevIds")
        if (matterDevIds.isEmpty()) {
            it.takeIf { it.isActive }?.resume(listOf())
            return@suspendCancellableCoroutine
        }
        ThingHomeSdk.getFabricManager().getDevicesFabricNodes(matterDevIds, object :
            IThingDataCallback<ArrayList<DeviceNodeBean>> {
            override fun onSuccess(result: ArrayList<DeviceNodeBean>?) {
                Timber.tag(TAG).i("initDevicesFabricNodes onSuccess: size->${result?.size}")
                it.takeIf { it.isActive }?.resume(result)
            }

            override fun onError(code: String?, msg: String?) {
                Timber.tag(TAG).e("initDevicesFabricNodes onError: code:$code,msg:$msg")
                it.takeIf { it.isActive }?.resume(null)
            }

        })
    }
}