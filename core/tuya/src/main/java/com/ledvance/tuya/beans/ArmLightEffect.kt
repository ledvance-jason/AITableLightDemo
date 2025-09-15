package com.ledvance.tuya.beans

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/15 14:48
 * Describe : ArmLightEffect
 */
enum class ArmLightEffect(val value: Int) {
    Exit(0x00),
    EyeCare(0x01),
    ColorMood(0x02),
    FocusReminder(0x05), ;

    companion object {
        fun of(value: Int): ArmLightEffect {
            return entries.find { it.value == value } ?: Exit
        }
    }
}