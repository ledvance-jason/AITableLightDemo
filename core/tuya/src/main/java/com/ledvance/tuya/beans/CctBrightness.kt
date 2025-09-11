package com.ledvance.tuya.beans

import kotlinx.serialization.Serializable

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/11 16:01
 * Describe : CctBrightness
 */
@Serializable
data class CctBrightness(
    val cct: Int,
    val brightness: Int
)