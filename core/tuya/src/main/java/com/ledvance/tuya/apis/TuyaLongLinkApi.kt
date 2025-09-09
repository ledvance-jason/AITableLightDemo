package com.ledvance.tuya.apis

import com.ledvance.tuya.apis.domain.ITuyaLongLinkApi
import com.ledvance.tuya.beans.DeviceState
import com.ledvance.tuya.beans.TuyaHomeChangeState
import com.ledvance.tuya.beans.toDeviceState
import com.ledvance.tuya.ktx.getDeviceSwitchDp
import com.ledvance.tuya.ktx.isTuyaLinkDevice
import com.ledvance.utils.extensions.updateMap
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.api.IThingHomeStatusListener
import com.thingclips.smart.sdk.api.IDeviceListener
import com.thingclips.smart.sdk.api.IThingLinkDeviceListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/3 10:33
 * Describe : TuyaLongLinkApi
 */
@Singleton
internal class TuyaLongLinkApi @Inject constructor() : ITuyaLongLinkApi {
    private val TAG = "TuyaLongLinkApi"

    private val homeDeviceStateFlow = MutableStateFlow<Map<String, DeviceState>>(mapOf())

    override suspend fun getHomeStatusFlow(homeId: Long) = callbackFlow {
        Timber.tag(TAG).i("getHomeStatusFlow: homeId->$homeId")
        val tuyaHomeImpl = ThingHomeSdk.newHomeInstance(homeId)
        val homeDeviceList = tuyaHomeImpl.homeBean?.deviceList ?: listOf()
        homeDeviceStateFlow.update {
            homeDeviceList.associate { it.devId to it.toDeviceState() }
        }
        val tuyaDevicesImpl = homeDeviceList.map {
            ThingHomeSdk.newDeviceInstance(it.devId)
        }
        val tuyaLinkDevicesImpl = homeDeviceList.filter {
            it.isTuyaLinkDevice()
        }.map { ThingHomeSdk.newDeviceInstance(it.devId) }

        val deviceStatusListener = object : IDeviceListener {
            override fun onDpUpdate(
                devId: String?,
                dpCodeMap: Map<String?, Any?>?
            ) {
                Timber.tag(TAG).i("device status onDpUpdate: $devId,${dpCodeMap}")
                devId ?: return
                dpCodeMap ?: return
                trySend(TuyaHomeChangeState.DeviceDpUpdate(devId, dpCodeMap))
                updateDeviceSwitchState(devId, dpCodeMap)
            }

            override fun onRemoved(devId: String?) {
                Timber.tag(TAG).i("device status onDeviceRemoved: $devId")
            }

            override fun onStatusChanged(devId: String?, online: Boolean) {
                Timber.tag(TAG).i("device status onStatusChanged: $devId, online:$online")
                devId ?: return
                trySend(TuyaHomeChangeState.DeviceOnline(devId, online))
                homeDeviceStateFlow.updateMap {
                    val value = get(devId)?.copy(online = online) ?: return@updateMap
                    put(devId, value)
                }
            }

            override fun onNetworkStatusChanged(devId: String?, status: Boolean) {
                Timber.tag(TAG).i("device status onNetworkStatusChanged: $devId, status:$status")
                devId ?: return
                trySend(TuyaHomeChangeState.DeviceNetworkStatus(devId, status))
            }

            override fun onDevInfoUpdate(devId: String?) {
                Timber.tag(TAG).i("device status onDevInfoUpdate: $devId")
                devId ?: return
                trySend(TuyaHomeChangeState.DeviceInfoUpdate(devId))
            }

        }

        val homeStatusListener = object : IThingHomeStatusListener {
            override fun onDeviceAdded(devId: String?) {
                Timber.tag(TAG).i("home status onDeviceAdded: $devId")
                devId ?: return
                trySend(TuyaHomeChangeState.DeviceAdded(devId))
                val deviceBean = ThingHomeSdk.getDataInstance().getDeviceBean(devId)
                deviceBean?.also {
                    homeDeviceStateFlow.updateMap { put(devId, it.toDeviceState()) }
                }
            }

            override fun onDeviceRemoved(devId: String?) {
                Timber.tag(TAG).i("home status onDeviceRemoved: $devId")
                devId ?: return
                trySend(TuyaHomeChangeState.DeviceRemoved(devId))
                homeDeviceStateFlow.updateMap { remove(devId) }
            }

            override fun onGroupAdded(groupId: Long) {
                Timber.tag(TAG).i("home status onGroupAdded: $groupId")
                trySend(TuyaHomeChangeState.GroupAdded(groupId))
            }

            override fun onGroupRemoved(groupId: Long) {
                Timber.tag(TAG).i("home status onGroupRemoved: $groupId")
            }

            override fun onMeshAdded(meshId: String?) {
                Timber.tag(TAG).i("home status onMeshAdded: $meshId")
                meshId ?: return
                trySend(TuyaHomeChangeState.MeshAdded(meshId))
            }
        }

        val linkDeviceListener = IThingLinkDeviceListener { messageType, payload ->
            Timber.tag(TAG).i("link device onMessage: $messageType $payload")
        }

        tuyaLinkDevicesImpl.forEach { it.registerThingLinkMessageListener(linkDeviceListener) }
        tuyaDevicesImpl.forEach { it.registerDeviceListener(deviceStatusListener) }
        tuyaHomeImpl.registerHomeStatusListener(homeStatusListener)
        awaitClose {
            tuyaDevicesImpl.forEach { it.unRegisterDevListener() }
            tuyaLinkDevicesImpl.map { it.unRegisterThingLinkMessageListener() }
            tuyaHomeImpl.unRegisterHomeStatusListener(homeStatusListener)
            Timber.tag(TAG).i("getHomeStatusFlow: awaitClose")
        }
    }

    private fun updateDeviceSwitchState(devId: String, dpCodeMap: Map<String?, Any?>) {
        val deviceSwitchDp = ThingHomeSdk.getDataInstance().getDeviceBean(devId)
            ?.getDeviceSwitchDp() ?: return
        if (dpCodeMap.containsKey(deviceSwitchDp.code)) {
            val switch = dpCodeMap[deviceSwitchDp.code].toString().toBoolean()
            homeDeviceStateFlow.updateMap {
                val value = get(devId)?.copy(switch = switch) ?: return
                put(devId, value)
            }
        }
    }

    override fun getHomeDeviceStateFlow(): StateFlow<Map<String, DeviceState>> {
        return homeDeviceStateFlow
    }

    override fun updateHomeDeviceState(devId: String, switch: Boolean?, online: Boolean?) {
        val logSwitch = switch?.let { "switch:$it" } ?: ""
        val logOnline = online?.let { "online:$it" } ?: ""
        Timber.tag(TAG).i("updateHomeDeviceState: $devId($logSwitch $logOnline)")
        homeDeviceStateFlow.updateMap {
            val deviceState = get(devId) ?: ThingHomeSdk.getDataInstance()
                .getDeviceBean(devId)?.toDeviceState() ?: return
            val value = deviceState.copy(
                switch = switch ?: deviceState.switch,
                online = online ?: deviceState.online
            )
            put(devId, value)
        }
    }

}