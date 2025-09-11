package com.ledvance.tuya.utils

import kotlin.math.roundToInt

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/11 15:23
 * Describe : ValueConverter
 */
object ValueConverter {

    /**
     * 通用范围转换工具（支持四舍五入）
     * @param value 原始输入值
     * @param inMin 输入范围的最小值（如 0）
     * @param inMax 输入范围的最大值（如 100）
     * @param outMin 输出范围的最小值（如 0）
     * @param outMax 输出范围的最大值（如 254）
     * @return 转换后并四舍五入的整数结果
     */
    fun convertRange(
        value: Int,
        inMin: Int,
        inMax: Int,
        outMin: Int,
        outMax: Int
    ): Int {
        // 1. 先限制输入值在 [inMin, inMax] 范围内（避免超出范围导致转换异常）
        val clampedX = value.coerceIn(inMin, inMax)
        // 2. 线性映射公式计算（用 Double 确保精度，避免整数除法丢失小数）
        val convertedDouble =
            (clampedX - inMin).toDouble() * (outMax - outMin) / (inMax - inMin) + outMin
        // 3. 四舍五入为整数并返回
        return convertedDouble.roundToInt()
    }

    /**
     * 将值在指定范围内进行反向映射
     * @param value 要取反的值
     * @param min 范围最小值
     * @param max 范围最大值
     * @return 取反后的值，保持在[min, max]范围内
     */
    fun invertRange(value: Int, min: Int, max: Int): Int {
        // 确保值在范围内
        val clampedValue = value.coerceIn(min, max)
        // 核心公式：最小值 + 最大值 - 当前值
        return min + max - clampedValue
    }
}