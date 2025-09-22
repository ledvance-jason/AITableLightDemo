package com.ledvance.tuya.command.controller.arm

import com.ledvance.tuya.beans.ArmLightEffectData
import com.ledvance.tuya.beans.ArmMode
import com.ledvance.tuya.beans.ArmSceneData
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
    suspend fun setCustomAction(actionName: String): Result<Boolean>
    fun getCustomAction(): Flow<String>
    fun getLightEffectFlow(): Flow<ArmLightEffectData?>
    suspend fun setLightEffect(lightEffectData: ArmLightEffectData): Result<Boolean>
    fun getSceneFlow(): Flow<ArmSceneData?>
    suspend fun setScene(sceneData: ArmSceneData): Result<Boolean>
    fun getModeFlow(): Flow<ArmMode>
    suspend fun setMode(mode: ArmMode): Result<Boolean>
}