package com.ledvance.ai.light.model

import com.ledvance.ai.light.ui.IFlowRowSectionItem

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 10:38
 * Describe : Mode
 */
enum class Mode(override val title: String) : IFlowRowSectionItem {
    Daily("Daily"),
    Reading("Reading"),
    Mood("Mood"),
    ;

    companion object {
        val allMode = listOf(
            Daily,
            Reading,
            Mood,
        )
    }
}