package com.ledvance.tuya.beans

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/17 11:13
 * Describe : ArmSceneData
 */
data class ArmSceneData(
    val scene: ArmScene,
    val enable: Boolean
)

fun ArmSceneData.toSceneStruct() = SceneStruct(scene.value, enable)
