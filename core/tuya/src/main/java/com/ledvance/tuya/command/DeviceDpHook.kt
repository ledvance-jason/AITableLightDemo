package com.ledvance.tuya.command

import android.os.SystemClock
import com.ledvance.tuya.beans.TuyaHomeChangeState
import com.ledvance.tuya.command.dps.DeviceDp
import com.ledvance.tuya.data.repo.ITuyaRepo
import com.ledvance.utils.extensions.updateMap
import com.thingclips.smart.sdk.bean.DeviceBean
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/10 18:01
 * Describe : DeviceDpHook
 */
internal class DeviceDpHook(
    private val device: DeviceBean,
    private val tuyaRepo: ITuyaRepo,
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO)
) {
    private val TAG = "DeviceDpHook"
    private val deviceDpsFlow = MutableStateFlow<Map<String, Any?>>(mapOf())

    private val deviceDpSendTimeMap by lazy {
        mutableMapOf<String, Long>()
    }

    init {
        scope.launch {
            val dps = device.getDps() ?: mapOf()
            Timber.tag(TAG).i("init dps: $dps")
            deviceDpsFlow.updateMap { putAll(dps) }
        }
        scope.launch {
            tuyaRepo.getLongLinkApi().getDeviceStatusFlow().collect { deviceStatus ->
                if (deviceStatus !is TuyaHomeChangeState.DeviceDpUpdate) {
                    return@collect
                }
                val (deviceId, dpIds) = deviceStatus
                if (deviceId != device.devId) {
                    return@collect
                }
                val updateDpIds = dpIds.filter {
                    // 如果发送的成功DP两秒内的不做长连接更新
                    SystemClock.elapsedRealtime() - (deviceDpSendTimeMap[it.key] ?: 0) > 2000
                }
                if (updateDpIds.isEmpty()) {
                    return@collect
                }
                Timber.tag(TAG).i("${device.devId} update dps: $updateDpIds")
                deviceDpsFlow.updateMap { putAll(updateDpIds) }
            }
        }
    }

    fun <T> useDp(dp: DeviceDp?, defaultValue: T? = null): Dp<T> {
        Timber.tag(TAG).i("useDp(${device.devId}): dp->$dp, defaultValue->$defaultValue")
        if (dp == null) {
            val emptyFlow = flowOf(defaultValue).filterNotNull()
            return Dp(emptyFlow, { Result.failure(Throwable("Dp is null")) })
        }

        val dpId = dp.dpId.toString()
        val dpFlow = deviceDpsFlow
            .mapNotNull { it[dpId] as? T ?: defaultValue }
            .distinctUntilChanged()
            .onEach { value ->
                Timber.tag(TAG).i("useDp(${device.devId}): dpId->$dpId, value->$value")
            }
        val setDpValue: suspend (T) -> Result<Boolean> = { value ->
            Timber.tag(TAG).i("useDp(${device.devId}): setDpValue dpId->${dpId} $value")
            val result = tuyaRepo.getDeviceApi().publishDps(
                devId = device.devId,
                command = DeviceCommandBuilder(device)
                    .addDp(dp, value ?: "")
                    .buildJson()
            )
            if (result.isSuccess) {
                deviceDpSendTimeMap.put(dpId, SystemClock.elapsedRealtime())
                deviceDpsFlow.updateMap { put(dpId, value) }
            }
            result
        }
        return Dp(dpFlow, setDpValue)
    }

    fun release() {
        scope.cancel()
    }
}