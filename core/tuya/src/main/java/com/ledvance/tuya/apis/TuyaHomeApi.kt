package com.ledvance.tuya.apis

import com.ledvance.tuya.apis.domain.ITuyaHomeApi
import com.ledvance.utils.extensions.toJson
import com.thingclips.smart.api.service.MicroServiceManager
import com.thingclips.smart.commonbiz.bizbundle.family.api.AbsBizBundleFamilyService
import com.thingclips.smart.commonbiz.relation.api.AbsRelationService
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.bean.HomeBean
import com.thingclips.smart.home.sdk.callback.IThingGetHomeListCallback
import com.thingclips.smart.home.sdk.callback.IThingHomeResultCallback
import com.thingclips.smart.sdk.bean.DeviceBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import kotlin.coroutines.resume

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/3 09:34
 * Describe : TuyaHomeApi
 */
internal class TuyaHomeApi @Inject constructor() : ITuyaHomeApi {
    private val TAG = "TuyaHomeApi"

    private val deviceListFlow = MutableStateFlow<List<DeviceBean>>(emptyList())

    override fun shiftCurrentHome(homeId: Long, homeName: String?) {
        val familyService =
            MicroServiceManager.getInstance().findServiceByInterface<AbsBizBundleFamilyService>(
                AbsBizBundleFamilyService::class.java.name
            )
        Timber.tag(TAG).i("shiftCurrentHome: $homeId $homeName")
        familyService.shiftCurrentFamily(homeId, homeName)
    }

    override fun getCurrentHomeId(): Long {
        val relationService =
            MicroServiceManager.getInstance().findServiceByInterface<AbsRelationService>(
                AbsRelationService::class.java.name
            )
        val homeId = relationService?.currentGid ?: 0
        Timber.tag(TAG).i("getCurrentFamilyId: $homeId")
        return homeId
    }

    override fun updateCurrentHomeDevices() {
        val currentHomeId = getCurrentHomeId()
        if (currentHomeId == 0L) {
            return
        }
        deviceListFlow.update { getHomeDeviceList(currentHomeId) }
    }

    override suspend fun getHomeList() = suspendCancellableCoroutine {
        Timber.tag(TAG).i("getHomeList: call")
        ThingHomeSdk.getHomeManagerInstance().queryHomeList(object : IThingGetHomeListCallback {
            override fun onSuccess(homeBeans: List<HomeBean?>?) {
                Timber.tag(TAG).i("getHomeList onSuccess ${homeBeans.toJson()}")
                it.takeIf { it.isActive }?.resume(homeBeans)
            }

            override fun onError(errorCode: String?, error: String?) {
                Timber.tag(TAG).e("getHomeList onError code:$errorCode,error:$error")
                it.takeIf { it.isActive }?.resume(null)
            }
        })
    }

    override fun getDeviceListFlow(): StateFlow<List<DeviceBean>> {
        return deviceListFlow
    }

    override suspend fun refreshCurrentHome() {
        val currentHomeId = getCurrentHomeId()
        Timber.tag(TAG).i("refreshCurrentHome: currentHomeId->$currentHomeId")
        if (currentHomeId == 0L) {
            return
        }
        getHomeDetail(currentHomeId, preferCache = false)
        updateCurrentHomeDevices()
    }

    override suspend fun getHomeDetail(homeId: Long, preferCache: Boolean) =
        withContext(Dispatchers.IO) {
            Timber.tag(TAG).i("getHomeDetail($homeId): preferCache->$preferCache")
            val homeDetail = if (preferCache) {
                getHomeDetailCache(homeId)
            } else null
            return@withContext homeDetail ?: getHomeDetailRemote(homeId)
            ?: getHomeDetailCache(homeId)
        }

    private suspend fun getHomeDetailRemote(homeId: Long) = suspendCancellableCoroutine {
        ThingHomeSdk.newHomeInstance(homeId).getHomeDetail(object : IThingHomeResultCallback {
            override fun onSuccess(bean: HomeBean?) {
                Timber.tag(TAG).i("getHomeDetail onSuccess ${bean.toJson()}")
                it.takeIf { it.isActive }?.resume(bean)
            }

            override fun onError(errorCode: String?, errorMsg: String?) {
                Timber.tag(TAG).e("getHomeDetail onError code:$errorCode,error:$errorMsg")
                it.takeIf { it.isActive }?.resume(null)
            }
        })
    }

    private suspend fun getHomeDetailCache(homeId: Long) = suspendCancellableCoroutine {
        ThingHomeSdk.newHomeInstance(homeId).getHomeLocalCache(object : IThingHomeResultCallback {
            override fun onSuccess(bean: HomeBean?) {
                Timber.tag(TAG).i("getHomeDetailCache onSuccess ${bean.toJson()}")
                it.takeIf { it.isActive }?.resume(bean)
            }

            override fun onError(errorCode: String?, errorMsg: String?) {
                Timber.tag(TAG).e("getHomeDetailCache onError code:$errorCode,error:$errorMsg")
                it.takeIf { it.isActive }?.resume(null)
            }
        })
    }

    private fun getHomeDeviceList(homeId: Long): List<DeviceBean> {
        val deviceList = ThingHomeSdk.getDataInstance().getHomeDeviceList(homeId) ?: listOf()
        Timber.tag(TAG).i("getHomeDeviceList deviceList ${deviceList.size}")
        return deviceList.sortedBy { it.displayOrder }
    }

}