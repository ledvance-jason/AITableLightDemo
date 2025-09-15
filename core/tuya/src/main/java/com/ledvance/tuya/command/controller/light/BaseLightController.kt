package com.ledvance.tuya.command.controller.light

import android.os.SystemClock
import com.ledvance.tuya.beans.CctBrightness
import com.ledvance.tuya.beans.Hsv
import com.ledvance.tuya.beans.WorkMode
import com.ledvance.tuya.command.controller.BaseController
import com.ledvance.tuya.ktx.getBrightnessDp
import com.ledvance.tuya.ktx.getCctDp
import com.ledvance.tuya.ktx.getControlDataDp
import com.ledvance.tuya.ktx.getDeviceSwitchDp
import com.ledvance.tuya.ktx.getHsvDp
import com.ledvance.tuya.ktx.getWorkModeDp
import com.ledvance.tuya.ktx.isSupportColorMode
import com.ledvance.tuya.ktx.isSupportWhiteMode
import com.ledvance.tuya.ktx.toHex
import com.thingclips.smart.sdk.bean.DeviceBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.sample
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/11 13:12
 * Describe : BaseLightController
 */
@OptIn(FlowPreview::class)
internal abstract class BaseLightController(device: DeviceBean) :
    BaseController(device), ILightController {

    private val switchDp = deviceDpHook.useDp<Boolean>(device.getDeviceSwitchDp())
    private val workModeDp = deviceDpHook.useDp<String>(device.getWorkModeDp())
    private val controlDataDp = deviceDpHook.useDp<String>(device.getControlDataDp())
    protected val brightnessDp = deviceDpHook.useDp<Int>(device.getBrightnessDp())
    protected val cctDp = deviceDpHook.useDp<Int>(device.getCctDp())
    protected val hsvDp = deviceDpHook.useDp<String>(device.getHsvDp())
    private val controlDataFlow = MutableStateFlow<Pair<Hsv?, CctBrightness?>>(null to null)
    private val isControlDataActive = MutableStateFlow(0L)

    init {
        scope.launch(Dispatchers.IO) {
            controlDataFlow.sample(500)
                .filter {
                    val time = SystemClock.elapsedRealtime() - isControlDataActive.value
                    time > 600
                }
                .collect { (hsv, cctBrightness) ->
                    sendControlData(hsv, cctBrightness)
                }
        }
    }

    protected fun discardControlData() {
        isControlDataActive.update { SystemClock.elapsedRealtime() }
    }

    override fun getSwitchFlow(): Flow<Boolean> {
        return switchDp.dpFlow
    }

    override suspend fun setSwitch(value: Boolean): Result<Boolean> {
        return switchDp.setDpValue(value)
    }

    override fun getWorkModeFlow(): Flow<WorkMode> {
        return workModeDp.dpFlow.map { WorkMode.of(it) }
    }

    override suspend fun setWorkMode(workMode: WorkMode): Result<Boolean> {
        return workModeDp.setDpValue(workMode.value)
    }

    override suspend fun setCctBrightness(cctBrightness: CctBrightness): Result<Boolean> {
        discardControlData()
        val (cct, brightness) = cctBrightness
        val cctResult = cctDp.sendDp(cct)
        val brightnessResult = brightnessDp.sendDp(brightness)
        if (cctResult.isSuccess && brightnessResult.isSuccess) {
            return Result.success(true)
        }
        return if (cctResult.isSuccess) {
            brightnessResult
        } else cctResult
    }


    override fun controlData(hsv: Hsv?, cctBrightness: CctBrightness?) {
        controlDataFlow.update { hsv to cctBrightness }
    }

    /**
     * h:0~360,s:0~100,v:1~100,cct:0~100,brightness:1~100
     */
    private fun sendControlData(hsv: Hsv?, cctBrightness: CctBrightness?) {
        if (hsv == null && cctBrightness == null) {
            return
        }
        scope.launch(Dispatchers.IO) {
            // 跳变：0 //渐变：1
            val changeCode = "1"
            // 色度(h)：0~360，0X0000~0X0168; 饱和度(s)：0~1000, 0X0000~0X03E8; 明度(v)：0~1000，0X0000~0X03E8
            val (h, s, v) = hsv ?: Hsv(0, 0, 0)
            // 色温值(cct)：0~1000，0X0000~0X03E8; 白光亮度(brightness)：0~1000，0X0000~0X03E8;
            val (cct, brightness) = cctBrightness ?: CctBrightness(0, 0)
            // changeCode:1,h:4,s:4,v:4,brightness:4,cct:4; 数字代表字符
            val hHex = h.toShort().toHex()
            val sHex = (s * 10).toShort().toHex()
            val vHex = (v * 10).toShort().toHex()
            val cctHex = (cct * 10).toShort().toHex()
            val bHex = (brightness * 10).toShort().toHex()
            val command = "$changeCode$hHex$sHex$vHex$bHex$cctHex"
            controlDataDp.sendDp(command)
        }
    }

    override fun isSupportColorMode(): Boolean {
        return device.isSupportColorMode()
    }

    override fun isSupportWhiteMode(): Boolean {
        return device.isSupportWhiteMode()
    }
}

