package com.ledvance.tuya.beans

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/15 14:49
 * Describe : ArmMode
 */
enum class ArmMode(val value: Int) {
    Daily(0x01),
    Reading(0x02),
    Mood(0x03), ;

    companion object {
        fun of(value: Int): ArmMode {
            return entries.find { it.value == value } ?: Daily
        }
    }
}