package com.ledvance.utils.extensions

import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/6/20 08:49
 * Describe : NumberExtensions
 */

fun Double.roundTo(decimals: Int = 1): Double {
    return BigDecimal(this)
        .setScale(decimals, RoundingMode.HALF_UP)
        .toDouble()
}