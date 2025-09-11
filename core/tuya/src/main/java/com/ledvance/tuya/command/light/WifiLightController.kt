package com.ledvance.tuya.command.light

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
import kotlinx.coroutines.flow.map
import timber.log.Timber

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/11 13:25
 * Describe : LightController
 */
internal class WifiLightController(device: DeviceBean) : BaseLightController(device) {


    override fun getHsvFlow(): Flow<Hsv> {
        return hsvDp.dpFlow.map { hsv ->
            val hsByteArray = hsv.toByteArray()
            val h = tryCatchReturn { hsByteArray[0].toIntValue() } ?: 0
            val s = tryCatchReturn { hsByteArray[1].toIntValue() } ?: 0
            val v = tryCatchReturn { hsByteArray[2].toIntValue() } ?: 1
            val newS = ValueConverter.convertRange(
                value = s,
                inMin = 0,
                inMax = 1000,
                outMin = 0,
                outMax = 100
            )
            val newV = ValueConverter.convertRange(
                value = v,
                inMin = 10,
                inMax = 1000,
                outMin = 1,
                outMax = 100
            )
            Timber.tag(TAG).i("getHsvFlow:$hsv h:$h,s:$newS,v:$newV")
            Hsv(h, newS, newV)
        }
    }

    override suspend fun setHsv(hsv: Hsv): Result<Boolean> {
        val (h, s, v) = hsv
        val newS = ValueConverter.convertRange(
            value = s,
            inMin = 0,
            inMax = 100,
            outMin = 0,
            outMax = 1000
        )
        val newV = ValueConverter.convertRange(
            value = v,
            inMin = 1,
            inMax = 100,
            outMin = 10,
            outMax = 1000
        )
        Timber.tag(TAG).i("setHsv: h:$h,s:$newS,v:$newV")
        val hHex = h.toShort().toHex()
        val sHex = newS.toShort().toHex()
        val vHex = newV.toShort().toHex()
        return hsvDp.setDpValue("$hHex$sHex$vHex")
    }

    override fun getCctBrightnessFlow(): Flow<CctBrightness> {
        return combine(cctDp.dpFlow, brightnessDp.dpFlow) { cct, brightness ->
            val newCct = ValueConverter.convertRange(
                value = cct,
                inMin = 0,
                inMax = 1000,
                outMin = 0,
                outMax = 100
            )
            val newBrightness = ValueConverter.convertRange(
                value = brightness,
                inMin = 10,
                inMax = 1000,
                outMin = 1,
                outMax = 100
            )
            Timber.tag(TAG).i("getCctBrightnessFlow: cct:$newCct,brightness:$newBrightness")
            CctBrightness(newCct, newBrightness)
        }
    }

    override suspend fun setCctBrightness(cctBrightness: CctBrightness): Result<Boolean> {
        val (cct, brightness) = cctBrightness
        val newCct = ValueConverter.convertRange(
            value = cct,
            inMin = 0,
            inMax = 100,
            outMin = 0,
            outMax = 1000
        )
        val newBrightness = ValueConverter.convertRange(
            value = brightness,
            inMin = 1,
            inMax = 100,
            outMin = 10,
            outMax = 1000
        )
        Timber.tag(TAG).i("setCctBrightness: cct:$newCct,brightness:$newBrightness")
        return super.setCctBrightness(CctBrightness(newCct, newBrightness))
    }
}