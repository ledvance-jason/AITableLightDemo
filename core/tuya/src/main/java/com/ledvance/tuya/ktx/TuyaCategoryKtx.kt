package com.ledvance.tuya.ktx

import com.ledvance.tuya.beans.TuyaCategory
import com.ledvance.tuya.beans.TuyaMainCategory
import com.thingclips.smart.sdk.bean.DeviceBean

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/11 13:21
 * Describe : TuyaCategoryKtx
 */
fun DeviceBean.getTuyaCategory() = TuyaCategory.fromDeviceCategory(deviceCategory)
fun DeviceBean.isTuyaCategoryOf(tuyaCategory: TuyaCategory): Boolean {
    return getTuyaCategory() == tuyaCategory
}

fun DeviceBean.isTuyaCategoryIn(vararg categories: TuyaCategory): Boolean {
    return getTuyaCategory() in categories
}

fun DeviceBean.isTuyaLighting() = getTuyaCategory().mainCategory == TuyaMainCategory.Lighting
