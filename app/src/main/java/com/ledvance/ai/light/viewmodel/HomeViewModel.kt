package com.ledvance.ai.light.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledvance.tuya.beans.TuyaHomeChangeState
import com.ledvance.tuya.command.DeviceCommandBuilder
import com.ledvance.tuya.command.dps.AITabLightDps
import com.ledvance.tuya.data.repo.ITuyaRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
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
    private val tuyaRepo: ITuyaRepo,
) : ViewModel() {
    private val TAG = "HomeViewModel"
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

    suspend fun shiftCurrentFamily() {
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
            collectHomeStatusFlow()
        }
        uiStateFlow.update { it.copy(loading = false) }
    }

    fun collectHomeStatusFlow() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentHomeId = tuyaRepo.getHomeApi().getCurrentHomeId()
            Timber.tag(TAG).i("collectHomeStatusFlow: currentFamilyId->$currentHomeId")
            if (currentHomeId == 0L) {
                return@launch
            }
            tuyaRepo.getLongLinkApi().getHomeStatusFlow(currentHomeId).collect { state ->
                when (state) {
                    is TuyaHomeChangeState.DeviceAdded -> {
                        tuyaRepo.getHomeApi().updateCurrentHomeDevices()
                    }

                    is TuyaHomeChangeState.DeviceDpUpdate -> {}
                    is TuyaHomeChangeState.DeviceInfoUpdate -> {}
                    is TuyaHomeChangeState.DeviceNetworkStatus -> {}
                    is TuyaHomeChangeState.DeviceOnline -> {}
                    is TuyaHomeChangeState.DeviceRemoved -> {
                        tuyaRepo.getHomeApi().updateCurrentHomeDevices()
                    }

                    is TuyaHomeChangeState.GroupAdded -> {}
                    is TuyaHomeChangeState.MeshAdded -> {}
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

    fun switch(devId: String, switch: Boolean) {
        viewModelScope.launch {
            uiStateFlow.update { it.copy(loading = true) }
            tuyaRepo.getDeviceApi().publishDps(
                devId = devId,
                command = DeviceCommandBuilder().add(
                    dp = AITabLightDps.SwitchDp,
                    value = switch
                ).buildJson()
            )
            uiStateFlow.update { it.copy(loading = false) }
        }
    }
}

data class HomeUIState(
    val loading: Boolean = false,
    val isRefreshing: Boolean = false
)