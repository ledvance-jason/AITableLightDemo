package com.ledvance.ai.light.model

import com.ledvance.ai.light.ui.IFlowRowSectionItem
import com.ledvance.tuya.beans.ArmCustomAction

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 10:46
 * Describe : CustomActions
 */
enum class CustomAction(override val title: String, override val id: Int) : IFlowRowSectionItem {
    Action1("Action 1", ArmCustomAction.Action1.value),
    Action2("Action 2", ArmCustomAction.Action2.value),
    Action3("Action 3", ArmCustomAction.Action3.value),
    Action4("Action 4", ArmCustomAction.Action4.value),
    Action5("Action 5", ArmCustomAction.Action5.value),
    Action6("Action 6", ArmCustomAction.Action6.value),
    ;

    companion object Companion {
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