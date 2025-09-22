package com.ledvance.tuya.command.controller.arm

import com.ledvance.tuya.beans.ArmLightEffectData
import com.ledvance.tuya.beans.ArmMode
import com.ledvance.tuya.beans.ArmSceneData
import com.ledvance.tuya.beans.SceneStruct
import com.ledvance.tuya.beans.toLightEffect
import com.ledvance.tuya.beans.toScene
import com.ledvance.tuya.beans.toSceneStruct
import com.ledvance.tuya.command.controller.BaseController
import com.ledvance.tuya.command.dps.AITableLightDps
import com.ledvance.utils.extensions.jsonAsOrNull
import com.ledvance.utils.extensions.toJson
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
    private val customActionDp = deviceDpHook.useDp(AITableLightDps.CustomActionDp,"")
    private val lightEffectDp = deviceDpHook.useDp(AITableLightDps.LightEffectDp, "")
    private val sceneDp = deviceDpHook.useDp(AITableLightDps.SceneDp, "")
    private val modeDp = deviceDpHook.useDp<Int>(AITableLightDps.ModeDp)

    override fun getVolumeFlow(): Flow<Int> {
        return volumeDp.dpFlow
    }

    override suspend fun setVolume(value: Int): Result<Boolean> {
        return volumeDp.sendDp(value)
    }

    override suspend fun setCustomAction(actionName: String): Result<Boolean> {
        return customActionDp.setDpValue(actionName)
    }

    override fun getCustomAction(): Flow<String> {
        return customActionDp.dpFlow.map {
            it
        }
    }

    override fun getLightEffectFlow(): Flow<ArmLightEffectData?> {
        return lightEffectDp.dpFlow.map {
            it.jsonAsOrNull<SceneStruct>()?.toLightEffect()
        }
    }

    override suspend fun setLightEffect(lightEffectData: ArmLightEffectData): Result<Boolean> {
        return lightEffectDp.setDpValue(lightEffectData.toSceneStruct().toJson() ?: "{}")
    }

    override fun getSceneFlow(): Flow<ArmSceneData?> {
        return sceneDp.dpFlow.map {
            it.jsonAsOrNull<SceneStruct>()?.toScene()
        }
    }

    override suspend fun setScene(sceneData: ArmSceneData): Result<Boolean> {
        return sceneDp.setDpValue(sceneData.toSceneStruct().toJson() ?: "{}")
    }

    override fun getModeFlow(): Flow<ArmMode> {
        return modeDp.dpFlow.map { ArmMode.of(it) }
    }

    override suspend fun setMode(mode: ArmMode): Result<Boolean> {
        return modeDp.setDpValue(mode.value)
    }
}