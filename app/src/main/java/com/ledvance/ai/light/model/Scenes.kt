package com.ledvance.ai.light.model

import com.ledvance.ai.light.ui.ISceneItem
import com.ledvance.ui.R

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 10:08
 * Describe : Scenes
 */
enum class Scenes(override val title: String, override val iconResId: Int, override val id: Int) :
    ISceneItem {
    Romantic("Romantic", R.drawable.icon_scenes_rose, 1),
    Reading("Reading", R.drawable.icon_scenes_book, 2),
    Relax("Relax", R.drawable.icon_scenes_palm_tree, 3),
    Party("Party", R.drawable.icon_scenes_tada, 4),
    Sunrise("Sunrise", R.drawable.icon_scenes_sunrise, 5),
    Rainbow("Rainbow", R.drawable.icon_scenes_rainbow, 6),
    EyeCare("Eye Care", R.drawable.icon_scenes_eyes, 7),
    ColorMood("Color Mood", R.drawable.icon_scenes_milky_way, 8),
    FocusReminder("Focus Reminder", R.drawable.icon_scenes_alarm_clock, 9),
    ;

    companion object {
        val allScenes = listOf(
            Romantic,
            Reading,
            Relax,
            Party,
            Sunrise,
            Rainbow
        )

        val lightEffect = listOf(
            EyeCare,
            ColorMood,
            FocusReminder,
        )
    }
}