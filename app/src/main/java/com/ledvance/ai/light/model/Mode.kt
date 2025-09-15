package com.ledvance.ai.light.model

import com.ledvance.ai.light.ui.IModeItem
import com.ledvance.tuya.beans.ArmMode
import com.ledvance.ui.R

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 10:38
 * Describe : Mode
 */
enum class Mode(
    override val title: String, override val iconResId: Int, override val id: Int,
) :
    IModeItem {
    Daily("Daily", R.drawable.icon_mode_daily, ArmMode.Daily.value),
    Reading("Reading", R.drawable.icon_mode_reading, ArmMode.Reading.value),
    Mood("Mood", R.drawable.icon_mode_mood, ArmMode.Mood.value),
    ;

    companion object {
        val allMode = listOf(
            Daily,
            Reading,
            Mood,
        )
    }
}