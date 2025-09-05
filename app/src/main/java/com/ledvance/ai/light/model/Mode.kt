package com.ledvance.ai.light.model

import com.ledvance.ai.light.ui.IModeItem
import com.ledvance.ui.R

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 10:38
 * Describe : Mode
 */
enum class Mode(override val title: String, override val iconResId: Int, override val id: Int) :
    IModeItem {
    Daily("Daily", R.drawable.icon_mode_daily, 1),
    Reading("Reading", R.drawable.icon_mode_reading, 2),
    Mood("Mood", R.drawable.icon_mode_mood, 3),
    ;

    companion object {
        val allMode = listOf(
            Daily,
            Reading,
            Mood,
        )
    }
}