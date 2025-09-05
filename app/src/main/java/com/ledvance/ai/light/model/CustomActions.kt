package com.ledvance.ai.light.model

import com.ledvance.ai.light.ui.IFlowRowSectionItem

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 10:46
 * Describe : CustomActions
 */
enum class CustomActions(override val title: String) : IFlowRowSectionItem {
    Action1("Action 1"),
    Action2("Action 2"),
    Action3("Action 3"),
    Action4("Action 4"),
    Action5("Action 5"),
    Action6("Action 6"),
    ;

    companion object {
        val allActions = listOf(
            Action1,
            Action2,
            Action3,
            Action4,
            Action5,
            Action6,
        )
    }
}