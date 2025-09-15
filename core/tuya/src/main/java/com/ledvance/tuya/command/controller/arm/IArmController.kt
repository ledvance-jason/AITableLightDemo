package com.ledvance.tuya.command.controller.arm

import com.ledvance.tuya.beans.ArmCustomAction
import com.ledvance.tuya.beans.ArmLightEffect
import com.ledvance.tuya.beans.ArmMode
import com.ledvance.tuya.beans.ArmScene
import kotlinx.coroutines.flow.Flow

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/15 11:45
 * Describe : IArmController
 */
interface IArmController {
    fun getVolumeFlow(): Flow<Int>
    suspend fun setVolume(value: Int): Result<Boolean>
    suspend fun setCustomAction(action: ArmCustomAction): Result<Boolean>
    fun getLightEffectFlow(): Flow<ArmLightEffect>
    suspend fun setLightEffect(lightEffect: ArmLightEffect): Result<Boolean>
    fun getSceneFlow(): Flow<ArmScene>
    suspend fun setScene(scene: ArmScene): Result<Boolean>
    fun getModeFlow(): Flow<ArmMode>
    suspend fun setMode(mode: ArmMode): Result<Boolean>
}