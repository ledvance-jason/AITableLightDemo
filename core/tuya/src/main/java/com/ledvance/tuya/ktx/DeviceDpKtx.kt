package com.ledvance.tuya.ktx

import com.ledvance.tuya.command.dps.DeviceDp
import com.thingclips.smart.sdk.bean.DeviceBean

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/11 11:31
 * Describe : DpKtx
 */
internal val allSwitchCodes = arrayOf("switch", "led_switch", "switch_led")
internal val allHsvCodes = arrayOf("hs_color_set", "colour_data")
internal val allCctCodes = arrayOf("color_temp_control", "temp_value")
internal val allBrightnessCodes = arrayOf("brightness_control", "bright_value")
internal val AllWorkModeCodes = arrayOf("work_mode")
internal val AllControlDataCodes = arrayOf("control_data")
internal val productSwitchDpMap by lazy {
    mutableMapOf<String?, DeviceDp>()
}

fun DeviceBean.getWorkModeDp(): DeviceDp {
    return getDp(*AllWorkModeCodes)
}

fun DeviceBean.getControlDataDp(): DeviceDp {
    return getDp(*AllControlDataCodes)
}

fun DeviceBean.getBrightnessDp(): DeviceDp {
    return getDp(*allBrightnessCodes)
}

fun DeviceBean.getCctDp(): DeviceDp {
    return getDp(*allCctCodes)
}

fun DeviceBean.getHsvDp(): DeviceDp {
    return getDp(*allHsvCodes)
}

fun DeviceBean.getDeviceSwitchDp(): DeviceDp {
    val pid = getProductId()
    val deviceSwitchDp = productSwitchDpMap[pid]
    if (deviceSwitchDp != null) {
        return deviceSwitchDp
    }
    return when {
        switchDp != 0 -> {
            val code = getDpSchemaList().find { it.id == "$switchDp" }?.code ?: ""
            val deviceDp = DeviceDp(dpId = switchDp, code = code)
            productSwitchDpMap[pid] = deviceDp
            deviceDp
        }

        else -> {
            val deviceDp = getDp(*allSwitchCodes)
            productSwitchDpMap[pid] = deviceDp
            deviceDp
        }
    }
}

fun DeviceBean.getDp(vararg dpCodes: String): DeviceDp {
    val dpSchema = getDpSchemaList().find { dpCodes.contains(it.code) }
    val dpId = dpSchema?.id?.toIntOrNull() ?: -1
    val dpCode = dpSchema?.code ?: "unknown"
    return DeviceDp(dpId = dpId, code = dpCode)
}