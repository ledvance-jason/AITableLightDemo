package com.ledvance.tuya.ktx

import com.ledvance.tuya.command.dps.DeviceDp
import com.thingclips.smart.android.device.bean.SchemaBean
import com.thingclips.smart.sdk.bean.DeviceBean
import timber.log.Timber

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/4 14:40
 * Describe : DeviceBeanKtx
 */

private val allSwitchCodes = listOf("switch")
private val productSwitchDpMap by lazy {
    mutableMapOf<String?, DeviceDp>()
}

fun DeviceBean.isSupportSwitch(): Boolean {
    val dpCodeSchemaMap = productBean?.schemaInfo?.dpCodeSchemaMap ?: mapOf()
    return allSwitchCodes.any { dpCodeSchemaMap.containsKey(it) }
}

fun DeviceBean.getSwitchState(): Boolean {
    val deviceSwitchDp = getDeviceSwitchDp()
    return getDps()?.get("${deviceSwitchDp.dpId}")?.toString().toBoolean()
}

fun DeviceBean.isTuyaLinkDevice(): Boolean = this.isSupportThingModelDevice

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
            val dpSchema = getDpSchemaList().find { allSwitchCodes.contains(it.code) }
            val dpId = dpSchema?.id?.toIntOrNull() ?: 1
            val dpCode = dpSchema?.code ?: "switch"
            val deviceDp = DeviceDp(dpId = dpId, code = dpCode)
            productSwitchDpMap[pid] = deviceDp
            deviceDp
        }
    }
}

fun DeviceBean.getDpSchemaList(): Collection<SchemaBean> {
    return getDpCodeSchemaMap().values
}

fun DeviceBean.getDpCodeSchemaMap(): Map<String, SchemaBean> {
    return productBean?.schemaInfo?.dpCodeSchemaMap ?: mapOf()
}