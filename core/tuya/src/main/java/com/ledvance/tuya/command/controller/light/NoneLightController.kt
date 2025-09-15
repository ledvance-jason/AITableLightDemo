package com.ledvance.tuya.command.controller.light

import com.ledvance.tuya.beans.CctBrightness
import com.ledvance.tuya.beans.Hsv
import com.ledvance.tuya.beans.WorkMode
import com.thingclips.smart.sdk.bean.DeviceBean
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/11 13:18
 * Describe : NoneLightController
 */
internal class NoneLightController : BaseLightController(DeviceBean()) {
    override fun getHsvFlow(): Flow<Hsv> {
        return flow { }
    }

    override fun getSwitchFlow(): Flow<Boolean> {
        return flow { }
    }

    override fun getWorkModeFlow(): Flow<WorkMode> {
        return flow { }
    }

    override fun getCctBrightnessFlow(): Flow<CctBrightness> {
        return flow { }
    }

    override suspend fun setHsv(hsv: Hsv): Result<Boolean> {
        return Result.success(true)
    }

    override suspend fun setCctBrightness(cctBrightness: CctBrightness): Result<Boolean> {
        return Result.success(true)
    }

    override suspend fun setSwitch(value: Boolean): Result<Boolean> {
        return Result.success(true)
    }

    override suspend fun setWorkMode(workMode: WorkMode): Result<Boolean> {
        return Result.success(true)
    }
}