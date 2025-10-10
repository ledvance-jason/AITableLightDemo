package com.ledvance.tuya.ktx

import android.content.Context
import androidx.core.os.bundleOf
import com.thingclips.smart.android.device.bean.SchemaBean
import com.thingclips.smart.api.router.UrlBuilder
import com.thingclips.smart.api.router.UrlRouter
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

fun DeviceBean.gotoPanelMore(context: Context) {
    val bundle = bundleOf(
        "action" to "gotoPanelMore",
        "extra_panel_more_type" to 2,
        "extra_panel_dev_id" to devId,
        "extra_panel_name" to name,
        "extra_panel_group_id" to -1
    )
    val urlBuilder = UrlBuilder(context, "panelAction")
        .putExtras(bundle)
    UrlRouter.execute(urlBuilder)
}

