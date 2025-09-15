package com.ledvance.tuya.command.controller.arm

import com.ledvance.tuya.beans.ArmCustomAction
import com.ledvance.tuya.beans.ArmLightEffect
import com.ledvance.tuya.beans.ArmMode
import com.ledvance.tuya.beans.ArmScene
import com.ledvance.tuya.command.controller.BaseController
import com.ledvance.tuya.command.dps.AITableLightDps
import com.thingclips.smart.sdk.bean.DeviceBean
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/15 11:26
 * Describe : ArmController
 */
internal class ArmController(device: DeviceBean) : BaseController(device), IArmController {
    private val volumeDp = deviceDpHook.useDp<Int>(AITableLightDps.VolumeDp)
    private val customActionDp = deviceDpHook.useDp<Int>(AITableLightDps.CustomActionDp)
    private val lightEffectDp = deviceDpHook.useDp<Int>(AITableLightDps.LightEffectDp)
    private val sceneDp = deviceDpHook.useDp<Int>(AITableLightDps.ScenesDp)
    private val modeDp = deviceDpHook.useDp<Int>(AITableLightDps.ModeDp)

    override fun getVolumeFlow(): Flow<Int> {
        return volumeDp.dpFlow
    }

    override suspend fun setVolume(value: Int): Result<Boolean> {
        return volumeDp.sendDp(value)
    }

    override suspend fun setCustomAction(action: ArmCustomAction): Result<Boolean> {
        return customActionDp.setDpValue(action.value)
    }

    override fun getLightEffectFlow(): Flow<ArmLightEffect> {
        return lightEffectDp.dpFlow.map { ArmLightEffect.of(it) }
    }

    override suspend fun setLightEffect(lightEffect: ArmLightEffect): Result<Boolean> {
        return lightEffectDp.setDpValue(lightEffect.value)
    }

    override fun getSceneFlow(): Flow<ArmScene> {
        return sceneDp.dpFlow.map { ArmScene.of(it) }
    }

    override suspend fun setScene(scene: ArmScene): Result<Boolean> {
        return sceneDp.setDpValue(scene.value)
    }

    override fun getModeFlow(): Flow<ArmMode> {
        return modeDp.dpFlow.map { ArmMode.of(it) }
    }

    override suspend fun setMode(mode: ArmMode): Result<Boolean> {
        return modeDp.setDpValue(mode.value)
    }
}