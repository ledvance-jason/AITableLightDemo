package com.ledvance.tuya.command.controller.light

import com.ledvance.tuya.beans.CctBrightness
import com.ledvance.tuya.beans.Hsv
import com.ledvance.tuya.ktx.toByteArray
import com.ledvance.tuya.ktx.toHex
import com.ledvance.tuya.ktx.toIntValue
import com.ledvance.tuya.utils.ValueConverter
import com.ledvance.utils.extensions.tryCatchReturn
import com.thingclips.smart.sdk.bean.DeviceBean
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import timber.log.Timber

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/11 13:16
 * Describe : MatterController
 */
internal class MatterLightController(device: DeviceBean) : BaseLightController(device) {

    override fun getCctBrightnessFlow(): Flow<CctBrightness> {
        return combine(cctDp.dpFlow, brightnessDp.dpFlow) { cct, brightness ->
            val newCct = ValueConverter.convertRange(
                value = ValueConverter.invertRange(cct, 153, 370),
                inMin = 153,
                inMax = 370,
                outMin = 0,
                outMax = 100
            )
            val newBrightness = ValueConverter.convertRange(
                value = brightness,
                inMin = 1,
                inMax = 254,
                outMin = 1,
                outMax = 100
            )
            Timber.tag(TAG).i("getCctBrightnessFlow: cct:$newCct,brightness:$newBrightness")
            CctBrightness(newCct, newBrightness)
        }
    }

    override fun getHsvFlow(): Flow<Hsv> {
        return combine(hsvDp.dpFlow, brightnessDp.dpFlow) { hs, brightness ->
            val hsByteArray = hs.toByteArray()
            val h = tryCatchReturn { hsByteArray[0].toIntValue() } ?: 0
            val s = tryCatchReturn { hsByteArray[1].toIntValue() } ?: 0
            val newH = ValueConverter.convertRange(
                value = h,
                inMin = 0,
                inMax = 254,
                outMin = 0,
                outMax = 360
            )
            val newS = ValueConverter.convertRange(
                value = s,
                inMin = 0,
                inMax = 254,
                outMin = 0,
                outMax = 100
            )
            val v = ValueConverter.convertRange(
                value = brightness,
                inMin = 1,
                inMax = 254,
                outMin = 1,
                outMax = 100
            )
            Timber.tag(TAG).i("getHsvFlow:$hs h:$newH,s:$newS,v:$v")
            Hsv(newH, newS, v)
        }
    }

    override suspend fun setHsv(hsv: Hsv): Result<Boolean> {
        discardControlData()
        val (h, s, v) = hsv
        val newH = ValueConverter.convertRange(
            value = h,
            inMin = 0,
            inMax = 360,
            outMin = 0,
            outMax = 254
        )
        val newS = ValueConverter.convertRange(
            value = s,
            inMin = 0,
            inMax = 100,
            outMin = 0,
            outMax = 254
        )
        val newV = ValueConverter.convertRange(
            value = v,
            inMin = 1,
            inMax = 100,
            outMin = 1,
            outMax = 254
        )
        val hHex = newH.toUByte().toByte().toHex()
        val sHex = newS.toUByte().toByte().toHex()
        Timber.tag(TAG).i("setHsv:$hHex$sHex h:$newH,s:$newS,v:$newV")
        val hsResult = hsvDp.sendDp("$hHex$sHex")
        val vResult = brightnessDp.sendDp(newV)
        if (hsResult.isSuccess && vResult.isSuccess) {
            return Result.success(true)
        }
        return if (hsResult.isFailure) hsResult else vResult
    }

    override suspend fun setCctBrightness(cctBrightness: CctBrightness): Result<Boolean> {
        discardControlData()
        val (cct, brightness) = cctBrightness
        val newCct = ValueConverter.convertRange(
            value = ValueConverter.invertRange(cct, 0, 100),
            inMin = 0,
            inMax = 100,
            outMin = 153,
            outMax = 370
        )
        val newBrightness = ValueConverter.convertRange(
            value = brightness,
            inMin = 1,
            inMax = 100,
            outMin = 1,
            outMax = 254
        )
        Timber.tag(TAG).i("setCctBrightness: cct:$newCct,brightness:$newBrightness")
        return super.setCctBrightness(CctBrightness(newCct, newBrightness))
    }

}