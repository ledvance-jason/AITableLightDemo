package com.ledvance.tuya.command.controller.light

import com.thingclips.smart.home.sdk.ThingHomeSdk

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/11 13:13
 * Describe : LightControllerFactory
 */
class LightControllerFactory {
    companion object Companion {
        fun createController(devId: String): ILightController {
            val device = ThingHomeSdk.getDataInstance().getDeviceBean(devId)
            return when {
                device == null -> NoneLightController()
                device.isMatter -> MatterLightController(device)
                else -> WifiLightController(device)
            }
        }
    }
}