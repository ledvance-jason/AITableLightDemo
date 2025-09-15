package com.ledvance.tuya.beans

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/15 14:47
 * Describe : ArmCustomAction
 */
enum class ArmCustomAction(val value: Int) {
    Action1(0x01),
    Action2(0x02),
    Action3(0x03),
    Action4(0x04),
    Action5(0x05),
    Action6(0x06), ;

    companion object {
        fun of(value: Int): ArmCustomAction {
            return entries.find { it.value == value } ?: Action1
        }
    }
}