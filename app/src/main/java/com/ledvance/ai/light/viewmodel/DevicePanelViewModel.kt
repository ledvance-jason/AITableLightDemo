package com.ledvance.ai.light.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledvance.tuya.command.DeviceCommandBuilder
import com.ledvance.tuya.data.repo.ITuyaRepo
import com.thingclips.smart.home.sdk.ThingHomeSdk
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/3 14:52
 * Describe : DevicePanelViewModel
 */
@HiltViewModel(assistedFactory = DevicePanelViewModel.Factory::class)
class DevicePanelViewModel @AssistedInject constructor(
    @Assisted private val devId: String,
    private val tuyaRepo: ITuyaRepo,
) : ViewModel() {
    private val _uiStateFlow = MutableStateFlow(DevicePanelUIState())
    val uiStateFlow: StateFlow<DevicePanelUIState> = _uiStateFlow

    val deviceStateMapFlow = tuyaRepo.getLongLinkApi().getHomeDeviceStateFlow().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = mapOf()
    )

    suspend fun delete(): Boolean {
        _uiStateFlow.update { it.copy(loading = true) }
        val result = tuyaRepo.getDeviceApi().deleteDevice(devId = devId, isReset = true)
        _uiStateFlow.update { it.copy(loading = false) }
        return result
    }

    suspend fun switch(switch: Boolean): Result<Boolean> {
        val device = ThingHomeSdk.getDataInstance().getDeviceBean(devId)
            ?: return Result.failure(Throwable("device is null!"))
        _uiStateFlow.update { it.copy(loading = true) }
        val result = tuyaRepo.getDeviceApi().publishDps(
            devId = devId,
            command = DeviceCommandBuilder(device)
                .addSwitch(switch)
                .buildJson()
        )
        if (result.isSuccess) {
            tuyaRepo.getLongLinkApi().updateHomeDeviceState(device.devId, switch = switch)
        }
        _uiStateFlow.update { it.copy(loading = false) }
        return result
    }

    data class DevicePanelUIState(
        val loading: Boolean = false,
    )

    @AssistedFactory
    interface Factory {
        fun create(devId: String): DevicePanelViewModel
    }
}