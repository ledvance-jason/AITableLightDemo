package com.ledvance.tuya.ktx

import com.thingclips.smart.sdk.bean.DeviceBean

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/4 14:40
 * Describe : DeviceBeanKtx
 */

private val allSwitchCodes = listOf("switch")

fun DeviceBean.isSupportSwitch(): Boolean {
    val dpCodeSchemaMap = productBean?.schemaInfo?.dpCodeSchemaMap ?: mapOf()
    return allSwitchCodes.any { dpCodeSchemaMap.containsKey(it) }
}

fun DeviceBean.getSwitchState(): Boolean {
    val dpCodeSchemaMap = productBean?.schemaInfo?.dpCodeSchemaMap ?: mapOf()
    val switchDpIds = dpCodeSchemaMap.filter { allSwitchCodes.contains(it.key) }.map { it.value.id }
    return getDps()?.any {
        switchDpIds.contains(it.key) && it.value.toString().toBoolean()
    } ?: false
}

fun DeviceBean.isTuyaLinkDevice(): Boolean = this.isSupportThingModelDevice