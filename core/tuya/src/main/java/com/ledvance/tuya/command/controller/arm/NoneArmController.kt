package com.ledvance.tuya.command.controller.arm

import com.ledvance.tuya.beans.ArmCustomAction
import com.ledvance.tuya.beans.ArmLightEffect
import com.ledvance.tuya.beans.ArmMode
import com.ledvance.tuya.beans.ArmScene
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

    override suspend fun setCustomAction(action: ArmCustomAction): Result<Boolean> {
        return Result.success(true)
    }

    override fun getLightEffectFlow(): Flow<ArmLightEffect> {
        return flow { }
    }

    override suspend fun setLightEffect(lightEffect: ArmLightEffect): Result<Boolean> {
        return Result.success(true)
    }

    override fun getSceneFlow(): Flow<ArmScene> {
        return flow { }
    }

    override suspend fun setScene(scene: ArmScene): Result<Boolean> {
        return Result.success(true)
    }

    override fun getModeFlow(): Flow<ArmMode> {
        return flow { }
    }

    override suspend fun setMode(mode: ArmMode): Result<Boolean> {
        return Result.success(true)
    }
}