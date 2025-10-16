package com.ledvance.ai.light.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ledvance.tuya.beans.ArmCustomAction
import com.ledvance.tuya.beans.ArmLightEffect
import com.ledvance.tuya.beans.ArmLightEffectData
import com.ledvance.tuya.beans.ArmMode
import com.ledvance.tuya.beans.ArmScene
import com.ledvance.tuya.beans.ArmSceneData
import com.ledvance.tuya.beans.CctBrightness
import com.ledvance.tuya.beans.Hsv
import com.ledvance.tuya.beans.WorkMode
import com.ledvance.tuya.command.controller.arm.ArmControllerFactory
import com.ledvance.tuya.command.controller.light.LightControllerFactory
import com.ledvance.tuya.data.repo.ITuyaRepo
import com.ledvance.tuya.ktx.gotoPanelMore
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
import timber.log.Timber

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
    private val TAG = "DevicePanelViewModel"
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
        armController.getSceneFlow(), armController.getLightEffectFlow(),
        armController.getVolumeFlow(), armController.getModeFlow(),
        armController.getCustomAction()
    ) { scene, lightEffect, volume, mode, customActionName ->
        val customAction = ArmCustomAction.of(customActionName)
        ArmUIState(mode, scene, lightEffect, volume, customAction)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ArmUIState()
    )

    init {
        val device = tuyaRepo.getDeviceApi().getDevice(devId)
        val dpCodes = device?.productBean?.schemaInfo?.dpCodeSchemaMap?.map { it.key }
        Timber.tag(TAG).i("device($devId) dpCodes: ${dpCodes?.joinToString(",")}")
    }

    suspend fun delete(): Result<Boolean> {
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

    suspend fun setHsv(h: Int, s: Int, v: Int): Result<Boolean> {
//        _uiStateFlow.update { it.copy(loading = true) }
        val result = lightController.setHsv(Hsv(h, s, v))
//        _uiStateFlow.update { it.copy(loading = false) }
        return result
    }

    suspend fun setCctBrightness(cct: Int, brightness: Int): Result<Boolean> {
//        _uiStateFlow.update { it.copy(loading = true) }
        val result = lightController.setCctBrightness(CctBrightness(cct, brightness))
//        _uiStateFlow.update { it.copy(loading = false) }
        return result
    }

    suspend fun setVolume(volume: Int): Result<Boolean> {
        _uiStateFlow.update { it.copy(loading = true) }
        val result = armController.setVolume(volume)
        _uiStateFlow.update { it.copy(loading = false) }
        return result
    }

    suspend fun setMode(modeId: Int): Result<Boolean> {
        _uiStateFlow.update { it.copy(loading = true) }
        val result = armController.setMode(ArmMode.of(modeId))
        _uiStateFlow.update { it.copy(loading = false) }
        return result
    }

    suspend fun setScene(sceneId: Int): Result<Boolean> {
        _uiStateFlow.update { it.copy(loading = true) }
        val sceneData = armUIStateFlow.value.sceneData
        val enable = if (sceneData?.scene?.value == sceneId) {
            sceneData.enable.not()
        } else true
        val scene = ArmScene.of(sceneId)
        val result = armController.setScene(ArmSceneData(scene, enable))
        _uiStateFlow.update { it.copy(loading = false) }
        return result
    }

    suspend fun setLightEffect(id: Int): Result<Boolean> {
        _uiStateFlow.update { it.copy(loading = true) }
        val lightEffectData = armUIStateFlow.value.lightEffectData
        val enable = if (lightEffectData?.lightEffect?.value == id) {
            lightEffectData.enable.not()
        } else true
        val lightEffect = ArmLightEffect.of(id)
        val result = armController.setLightEffect(ArmLightEffectData(lightEffect, enable))
        _uiStateFlow.update { it.copy(loading = false) }
        return result
    }

    suspend fun setCustomAction(actionName: String): Result<Boolean> {
        _uiStateFlow.update { it.copy(loading = true) }
        val result = armController.setCustomAction(actionName)
        _uiStateFlow.update { it.copy(loading = false) }
        return result
    }

    override fun onCleared() {
        super.onCleared()
        lightController.release()
    }

    fun gotoDeviceSettings(context: Context) {
        tuyaRepo.getDeviceApi().getDevice(devId)?.gotoPanelMore(context)
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
        val sceneData: ArmSceneData? = null,
        val lightEffectData: ArmLightEffectData? = null,
        val volume: Int = 100,
        val customAction: ArmCustomAction? = null
    )

    @AssistedFactory
    interface Factory {
        fun create(devId: String): DevicePanelViewModel
    }
}