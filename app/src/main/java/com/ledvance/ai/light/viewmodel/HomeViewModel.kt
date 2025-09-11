package com.ledvance.ai.light.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledvance.tuya.beans.TuyaHomeChangeState
import com.ledvance.tuya.command.DeviceCommandBuilder
import com.ledvance.tuya.data.repo.ITuyaRepo
import com.thingclips.smart.sdk.bean.DeviceBean
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/3 14:23
 * Describe : HomeViewModel
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val context: Application,
    private val tuyaRepo: ITuyaRepo,
) : ViewModel() {
    private val TAG = "HomeViewModel"
    private var longLinkJob: Job? = null
    val uiStateFlow = MutableStateFlow(HomeUIState())

    val deviceListFlow = tuyaRepo.getHomeApi().getDeviceListFlow().map {
        it.filter { it.parentId.isNullOrEmpty() }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf()
    )
    val deviceStateMapFlow = tuyaRepo.getLongLinkApi().getHomeDeviceStateFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = mapOf()
    )

    suspend fun shiftCurrentFamily() = withContext(Dispatchers.IO) {
        Timber.tag(TAG).i("shiftCurrentFamily begin")
        uiStateFlow.update { it.copy(loading = true) }
        val currentFamilyId = tuyaRepo.getHomeApi().getCurrentHomeId()
        val homeDetail = if (currentFamilyId != 0L) {
            tuyaRepo.getHomeApi().getHomeDetail(currentFamilyId)
        } else {
            val home = tuyaRepo.getHomeApi().getHomeList()?.firstOrNull()
            home?.let { tuyaRepo.getHomeApi().getHomeDetail(it.homeId) }
        }
        homeDetail?.run {
            tuyaRepo.getHomeApi().shiftCurrentHome(homeId, name)
            tuyaRepo.getMatterApi().initFabric(context, homeId)
            tuyaRepo.getMatterApi().initDevicesFabricNodes(homeId)
            collectHomeStatusFlow()
        }
        tuyaRepo.getHomeApi().updateCurrentHomeDevices()
        Timber.tag(TAG).i("shiftCurrentFamily: ${homeDetail?.homeId} end")
        uiStateFlow.update { it.copy(loading = false) }
    }

    suspend fun collectHomeStatusFlow() {
        longLinkJob?.run {
            cancel()
            // 避免长连接的回调取消过快 延迟一下
            delay(1000)
        }
        longLinkJob = viewModelScope.launch(Dispatchers.IO) {
            val currentHomeId = tuyaRepo.getHomeApi().getCurrentHomeId()
            Timber.tag(TAG).i("collectHomeStatusFlow: currentFamilyId->$currentHomeId")
            if (currentHomeId == 0L) {
                return@launch
            }
            tuyaRepo.getLongLinkApi().getHomeStatusFlow(currentHomeId).collect { state ->
                when (state) {
                    is TuyaHomeChangeState.DeviceAdded -> {
                        tuyaRepo.getHomeApi().updateCurrentHomeDevices()
                        viewModelScope.launch(Dispatchers.IO) { collectHomeStatusFlow() }
                    }

                    is TuyaHomeChangeState.DeviceDpUpdate -> {}
                    is TuyaHomeChangeState.DeviceInfoUpdate -> {
                        tuyaRepo.getHomeApi().updateCurrentHomeDevices()
                    }

                    is TuyaHomeChangeState.DeviceNetworkStatus -> {}
                    is TuyaHomeChangeState.DeviceOnline -> {}
                    is TuyaHomeChangeState.DeviceRemoved -> {
                        tuyaRepo.getHomeApi().updateCurrentHomeDevices()
                    }

                    is TuyaHomeChangeState.GroupAdded -> {}
                    is TuyaHomeChangeState.MeshAdded -> {}
                    else -> {}
                }
            }
        }
    }


    fun onRefresh() {
        viewModelScope.launch {
            uiStateFlow.update { it.copy(isRefreshing = true) }
            tuyaRepo.getHomeApi().refreshCurrentHome()
            collectHomeStatusFlow()
            uiStateFlow.update { it.copy(isRefreshing = false) }
        }
    }

    suspend fun switch(device: DeviceBean, switch: Boolean): Result<Boolean> {
        uiStateFlow.update { it.copy(loading = true) }
        val result = tuyaRepo.getDeviceApi().publishDps(
            devId = device.devId,
            command = DeviceCommandBuilder(device)
                .addSwitch(switch)
                .buildJson()
        )
        if (result.isSuccess) {
            tuyaRepo.getLongLinkApi().updateHomeDeviceState(device.devId, switch = switch)
        }
        uiStateFlow.update { it.copy(loading = false) }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        longLinkJob?.cancel()
    }
}

data class HomeUIState(
    val loading: Boolean = true,
    val isRefreshing: Boolean = false
)