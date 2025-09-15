package com.ledvance.ai.light.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledvance.tuya.beans.ArmCustomAction
import com.ledvance.tuya.beans.ArmLightEffect
import com.ledvance.tuya.beans.ArmMode
import com.ledvance.tuya.beans.ArmScene
import com.ledvance.tuya.beans.CctBrightness
import com.ledvance.tuya.beans.Hsv
import com.ledvance.tuya.beans.WorkMode
import com.ledvance.tuya.command.controller.arm.ArmControllerFactory
import com.ledvance.tuya.command.controller.light.LightControllerFactory
import com.ledvance.tuya.data.repo.ITuyaRepo
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
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
    private val armController by lazy {
        ArmControllerFactory.createController(devId)
    }
    private val _uiStateFlow = MutableStateFlow(DevicePanelUIState())
    val uiStateFlow: StateFlow<DevicePanelUIState> = _uiStateFlow

    val lightStateFlow: StateFlow<LightUIState> = combine(
        lightController.getSwitchFlow(), lightController.getWorkModeFlow(),
        lightController.getHsvFlow(), lightController.getCctBrightnessFlow()
    ) { switch, workMode, hsv, cctBrightness ->
        LightUIState(switch, workMode, hsv, cctBrightness)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = LightUIState()
    )

    val armUIStateFlow: StateFlow<ArmUIState> = combine(
        armController.getVolumeFlow(), armController.getModeFlow(),
        armController.getSceneFlow(), armController.getLightEffectFlow()
    ) { volume, mode, scene, lightEffect ->
        ArmUIState(mode, scene, lightEffect, volume)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ArmUIState()
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

    fun setVolume(volume: Int) {
        viewModelScope.launch {
            armController.setVolume(volume)
        }
    }

    suspend fun setMode(modeId: Int): Result<Boolean> {
        _uiStateFlow.update { it.copy(loading = true) }
        val result = armController.setMode(ArmMode.of(modeId))
        _uiStateFlow.update { it.copy(loading = false) }
        return result
    }

    suspend fun setScene(sceneId: Int): Result<Boolean> {
        _uiStateFlow.update { it.copy(loading = true) }
        val scene = if (sceneId == armUIStateFlow.value.scene.value) {
            ArmScene.Exit
        } else ArmScene.of(sceneId)
        val result = armController.setScene(scene)
        _uiStateFlow.update { it.copy(loading = false) }
        return result
    }

    suspend fun setLightEffect(id: Int): Result<Boolean> {
        _uiStateFlow.update { it.copy(loading = true) }
        val lightEffect = if (id == armUIStateFlow.value.lightEffect.value) {
            ArmLightEffect.Exit
        } else ArmLightEffect.of(id)
        val result = armController.setLightEffect(lightEffect)
        _uiStateFlow.update { it.copy(loading = false) }
        return result
    }

    suspend fun setCustomAction(actionId: Int): Result<Boolean> {
        _uiStateFlow.update { it.copy(loading = true) }
        val result = armController.setCustomAction(ArmCustomAction.of(actionId))
        _uiStateFlow.update { it.copy(loading = false) }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        lightController.release()
    }

    data class DevicePanelUIState(
        val loading: Boolean = false,
    )

    data class LightUIState(
        val switch: Boolean = false,
        val workMode: WorkMode = WorkMode.Colour,
        val hsv: Hsv = Hsv(0, 0, 100),
        val cctBrightness: CctBrightness = CctBrightness(0, 100)
    )

    data class ArmUIState(
        val mode: ArmMode = ArmMode.Daily,
        val scene: ArmScene = ArmScene.Exit,
        val lightEffect: ArmLightEffect = ArmLightEffect.Exit,
        val volume: Int = 100
    )

    @AssistedFactory
    interface Factory {
        fun create(devId: String): DevicePanelViewModel
    }
}