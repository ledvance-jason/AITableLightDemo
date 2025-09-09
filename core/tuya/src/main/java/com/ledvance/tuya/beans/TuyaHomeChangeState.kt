package com.ledvance.tuya.beans

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/3 10:41
 * Describe : TuyaHomeChangeState
 */
sealed interface TuyaHomeChangeState {
    data class DeviceDpUpdate(val devId: String, val dps: Map<String?, Any?>) : TuyaHomeChangeState
    data class DeviceRemoved(val devId: String) : TuyaHomeChangeState
    data class DeviceOnline(val devId: String, val online: Boolean) : TuyaHomeChangeState
    data class DeviceNetworkStatus(val devId: String, val status: Boolean) : TuyaHomeChangeState
    data class DeviceInfoUpdate(val devId: String) : TuyaHomeChangeState

    data class DeviceAdded(val devId: String) : TuyaHomeChangeState
    data class GroupAdded(val groupId: Long) : TuyaHomeChangeState
    data class MeshAdded(val meshId: String) : TuyaHomeChangeState
}