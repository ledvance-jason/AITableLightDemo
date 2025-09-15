package com.ledvance.tuya.command.controller.arm

import com.thingclips.smart.home.sdk.ThingHomeSdk

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/11 13:13
 * Describe : LightControllerFactory
 */
class ArmControllerFactory {
    companion object Companion {
        fun createController(devId: String): IArmController {
            val device = ThingHomeSdk.getDataInstance().getDeviceBean(devId)
            return when {
                device == null -> NoneArmController()
                else -> ArmController(device)
            }
        }
    }
}