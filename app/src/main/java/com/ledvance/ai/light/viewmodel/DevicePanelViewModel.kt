package com.ledvance.ai.light.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledvance.tuya.beans.CctBrightness
import com.ledvance.tuya.beans.Hsv
import com.ledvance.tuya.beans.WorkMode
import com.ledvance.tuya.command.light.LightControllerFactory
import com.ledvance.tuya.data.repo.ITuyaRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

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
    private val lightController by lazy {
        LightControllerFactory.createController(devId)
    }
    private val _uiStateFlow = MutableStateFlow(DevicePanelUIState())
    val uiStateFlow: StateFlow<DevicePanelUIState> = _uiStateFlow

    val deviceStateFlow: StateFlow<DeviceUIState> = combine(
        lightController.getSwitchFlow(), lightController.getWorkModeFlow(),
        lightController.getHsvFlow(), lightController.getCctBrightnessFlow()
    ) { switch, workMode, hsv, cctBrightness ->
        DeviceUIState(switch, workMode, hsv, cctBrightness)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DeviceUIState()
    )

    suspend fun delete(): Boolean {
        _uiStateFlow.update { it.copy(loading = true) }
        val result = tuyaRepo.getDeviceApi().deleteDevice(devId = devId, isReset = true)
        _uiStateFlow.update { it.copy(loading = false) }
        return result
    }

    suspend fun switch(switch: Boolean): Result<Boolean> {
        _uiStateFlow.update { it.copy(loading = true) }
        val result = lightController.setSwitch(switch)
        _uiStateFlow.update { it.copy(loading = false) }
        return result
    }

    suspend fun setWorkMode(workMode: WorkMode): Result<Boolean> {
        _uiStateFlow.update { it.copy(loading = true) }
        val result = lightController.setWorkMode(workMode)
        _uiStateFlow.update { it.copy(loading = false) }
        return result
    }

    fun controlHsv(h: Int, s: Int, v: Int) {
        lightController.controlData(hsv = Hsv(h, s, v))
    }

    fun controlCctBrightness(cct: Int, brightness: Int) {
        lightController.controlData(cctBrightness = CctBrightness(cct, brightness))
    }

    fun setHsv(h: Int, s: Int, v: Int) {
        viewModelScope.launch {
            lightController.setHsv(Hsv(h, s, v))
        }
    }

    fun setCctBrightness(cct: Int, brightness: Int) {
        viewModelScope.launch {
            lightController.setCctBrightness(CctBrightness(cct, brightness))
        }
    }

    override fun onCleared() {
        super.onCleared()
        lightController.release()
    }

    data class DevicePanelUIState(
        val loading: Boolean = false,
    )

    data class DeviceUIState(
        val switch: Boolean = false,
        val workMode: WorkMode = WorkMode.Colour,
        val hsv: Hsv = Hsv(0, 0, 100),
        val cctBrightness: CctBrightness = CctBrightness(0, 100)
    )

    @AssistedFactory
    interface Factory {
        fun create(devId: String): DevicePanelViewModel
    }
}