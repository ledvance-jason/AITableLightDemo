package com.ledvance.ai.light.model

import com.ledvance.ui.component.IRadioGroupItem

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 15:22
 * Describe : WorkModeSegment
 */
enum class WorkModeSegment(override val title: String, override val id: Int) : IRadioGroupItem {
    ColorMode("Color mode", id = 1),
    WhiteMode("White mode", id = 2),
    ;

    companion object {
        val allWorkModeSegment = listOf(
            ColorMode,
            WhiteMode,
        )
    }
}