package com.ledvance.tuya.beans

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/15 14:48
 * Describe : ArmScene
 */
enum class ArmScene(val value: Int) {
    Unknown(0x00),
    Romantic(0x01),
    Reading(0x02),
    Relax(0x03),
    Party(0x04),
    Sunrise(0x05),
    Rainbow(0x06), ;

    companion object {
        fun of(value: Int): ArmScene {
            return entries.find { it.value == value } ?: Unknown
        }
    }
}