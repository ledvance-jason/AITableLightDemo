package com.ledvance.tuya.beans

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/17 11:14
 * Describe : SceneStruct
 */
data class SceneStruct(
    val id: Int,
    val enable: Boolean
)

fun SceneStruct.toLightEffect() = ArmLightEffectData(
    lightEffect = ArmLightEffect.of(id),
    enable = enable
)

fun SceneStruct.toScene() = ArmSceneData(
    scene = ArmScene.of(id),
    enable = enable
)