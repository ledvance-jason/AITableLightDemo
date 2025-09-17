package com.ledvance.tuya.command.dps

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/4 14:07
 * Describe : AITableLightDps
 */
object AITableLightDps {
    val VolumeDp = DeviceDp(dpId = 107, code = "volume_set")
    val CustomActionDp = DeviceDp(dpId = 108, code = "custom_action")
    val LightEffectDp = DeviceDp(dpId = 109, code = "light_effect")
    val SceneDp = DeviceDp(dpId = 110, code = "scene")
    val ModeDp = DeviceDp(dpId = 111, code = "arm_mode")
}