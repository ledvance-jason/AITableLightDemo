package com.ledvance.tuya.ktx

import com.thingclips.smart.android.device.bean.SchemaBean
import com.thingclips.smart.sdk.bean.DeviceBean

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/4 14:40
 * Describe : DeviceBeanKtx
 */
fun DeviceBean.isSupportSwitch(): Boolean {
    return switchDp != 0 || isSupportDpCode(*allSwitchCodes)
}

fun DeviceBean.isSupportColorMode(): Boolean {
    return isSupportDpCode(*allHsvCodes)
}

fun DeviceBean.isSupportWhiteMode(): Boolean {
    return isSupportDpCode(*allBrightnessCodes) || isSupportDpCode(*allCctCodes)
}

fun DeviceBean.isSupportDpCode(vararg dpCode: String): Boolean {
    val dpCodeSchemaMap = productBean?.schemaInfo?.dpCodeSchemaMap ?: mapOf()
    return dpCode.any { dpCodeSchemaMap.containsKey(it) }
}

fun DeviceBean.getSwitchState(): Boolean {
    val deviceSwitchDp = getDeviceSwitchDp()
    return getDps()?.get("${deviceSwitchDp.dpId}")?.toString().toBoolean()
}

fun DeviceBean.isTuyaLinkDevice(): Boolean = this.isSupportThingModelDevice

fun DeviceBean.getDpSchemaList(): Collection<SchemaBean> {
    return getDpCodeSchemaMap().values
}

fun DeviceBean.getDpCodeSchemaMap(): Map<String, SchemaBean> {
    return productBean?.schemaInfo?.dpCodeSchemaMap ?: mapOf()
}