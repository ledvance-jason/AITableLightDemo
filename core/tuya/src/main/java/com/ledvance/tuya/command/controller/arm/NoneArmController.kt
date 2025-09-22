package com.ledvance.tuya.command.controller.arm

import com.ledvance.tuya.beans.ArmLightEffectData
import com.ledvance.tuya.beans.ArmMode
import com.ledvance.tuya.beans.ArmSceneData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/15 11:45
 * Describe : NoneArmController
 */
class NoneArmController : IArmController {
    override fun getVolumeFlow(): Flow<Int> {
        return flow { }
    }

    override suspend fun setVolume(value: Int): Result<Boolean> {
        return Result.success(true)
    }

    override suspend fun setCustomAction(actionName: String): Result<Boolean> {
        return Result.success(true)
    }

    override fun getCustomAction(): Flow<String> {
        return flow { }
    }

    override fun getLightEffectFlow(): Flow<ArmLightEffectData?> {
        return flow { }
    }

    override suspend fun setLightEffect(lightEffectData: ArmLightEffectData): Result<Boolean> {
        return Result.success(true)
    }

    override fun getSceneFlow(): Flow<ArmSceneData?> {
        return flow { }
    }

    override suspend fun setScene(sceneData: ArmSceneData): Result<Boolean> {
        return Result.success(true)
    }

    override fun getModeFlow(): Flow<ArmMode> {
        return flow { }
    }

    override suspend fun setMode(mode: ArmMode): Result<Boolean> {
        return Result.success(true)
    }
}