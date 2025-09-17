package com.ledvance.tuya.beans

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/17 11:13
 * Describe : ArmLightEffectData
 */
data class ArmLightEffectData(
    val lightEffect: ArmLightEffect,
    val enable: Boolean
)

fun ArmLightEffectData.toSceneStruct() = SceneStruct(lightEffect.value, enable)