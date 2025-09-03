package com.ledvance.tuya.apis

import com.ledvance.tuya.apis.domain.ITuyaLongLinkApi
import com.ledvance.tuya.beans.DeviceState
import com.ledvance.tuya.beans.TuyaHomeChangeState
import com.ledvance.tuya.beans.toDeviceState
import com.ledvance.tuya.ktx.getSwitchState
import com.ledvance.tuya.ktx.isTuyaLinkDevice
import com.ledvance.utils.extensions.updateMap
import com.thingclips.smart.home.sdk.ThingHomeSdk
import com.thingclips.smart.home.sdk.api.IThingHomeDeviceStatusListener
import com.thingclips.smart.home.sdk.api.IThingHomeStatusListener
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

    override fun getHomeStatusFlow(homeId: Long) = callbackFlow {
        Timber.tag(TAG).d("getHomeStatusFlow: homeId->$homeId")
        val tuyaHomeImpl = ThingHomeSdk.newHomeInstance(homeId)
        val homeDeviceList = tuyaHomeImpl.homeBean?.deviceList ?: listOf()
        homeDeviceStateFlow.update {
            homeDeviceList.associate { it.devId to it.toDeviceState() }
        }
        val tuyaLinkDevicesImpl = homeDeviceList.filter {
            it.isTuyaLinkDevice()
        }.map { ThingHomeSdk.newDeviceInstance(it.devId) }


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
        val homeDeviceStatusListener = object : IThingHomeDeviceStatusListener {
            override fun onDeviceDpUpdate(devId: String?, dpStr: String?) {
                Timber.tag(TAG).i("home device status onDpUpdate: $devId,${dpStr}")
                devId ?: return
                trySend(TuyaHomeChangeState.DeviceDpUpdate(devId, dpStr ?: ""))
                val deviceBean = ThingHomeSdk.getDataInstance().getDeviceBean(devId)
                deviceBean?.also {
                    homeDeviceStateFlow.updateMap {
                        val value = get(devId)?.copy(switch = it.getSwitchState()) ?: return
                        put(devId, value)
                    }
                }

            }

            override fun onDeviceStatusChanged(devId: String?, online: Boolean) {
                Timber.tag(TAG).i("home device status onStatusChanged: $devId, online:$online")
                devId ?: return
                trySend(TuyaHomeChangeState.DeviceOnline(devId, online))
                homeDeviceStateFlow.updateMap {
                    val value = get(devId)?.copy(online = online) ?: return@updateMap
                    put(devId, value)
                }
            }

            override fun onDeviceInfoUpdate(devId: String?) {
                Timber.tag(TAG).i("home device status onDevInfoUpdate: $devId")
                devId ?: return
                trySend(TuyaHomeChangeState.DeviceInfoUpdate(devId))
            }
        }

        val linkDeviceListener = IThingLinkDeviceListener { messageType, payload ->
            Timber.tag(TAG).i("link device onMessage: $messageType $payload")
        }
        tuyaLinkDevicesImpl.forEach { it.registerThingLinkMessageListener(linkDeviceListener) }
        tuyaHomeImpl.registerHomeDeviceStatusListener(homeDeviceStatusListener)
        tuyaHomeImpl.registerHomeStatusListener(homeStatusListener)
        awaitClose {
            tuyaLinkDevicesImpl.map { it.unRegisterThingLinkMessageListener() }
            tuyaHomeImpl.unRegisterHomeStatusListener(homeStatusListener)
            tuyaHomeImpl.unRegisterHomeDeviceStatusListener(homeDeviceStatusListener)
            Timber.tag(TAG).i("getHomeStatusFlow: awaitClose")
        }
    }

    override fun getHomeDeviceStateFlow(): StateFlow<Map<String, DeviceState>> {
        return homeDeviceStateFlow
    }

}