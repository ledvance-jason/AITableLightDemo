package com.ledvance.tuya.command.controller.light

import com.ledvance.tuya.beans.CctBrightness
import com.ledvance.tuya.beans.Hsv
import com.ledvance.tuya.beans.WorkMode
import kotlinx.coroutines.flow.Flow

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/11 13:58
 * Describe : ILightController
 */
interface ILightController {
    fun getSwitchFlow(): Flow<Boolean>
    suspend fun setSwitch(value: Boolean): Result<Boolean>
    fun getWorkModeFlow(): Flow<WorkMode>
    suspend fun setWorkMode(workMode: WorkMode): Result<Boolean>

    // h:0~360,s:0~100,v:1~100,cct:0~100,brightness:1~100
    fun controlData(hsv: Hsv? = null, cctBrightness: CctBrightness? = null)

    // h:0~360,s:0~100,v:1~100
    fun getHsvFlow(): Flow<Hsv>
    suspend fun setHsv(hsv: Hsv): Result<Boolean>

    // cct:0~100,brightness:1~100
    fun getCctBrightnessFlow(): Flow<CctBrightness>
    suspend fun setCctBrightness(cctBrightness: CctBrightness): Result<Boolean>
    fun isSupportColorMode(): Boolean
    fun isSupportWhiteMode(): Boolean
    fun release()
}