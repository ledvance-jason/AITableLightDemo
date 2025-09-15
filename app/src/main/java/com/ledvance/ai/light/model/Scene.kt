package com.ledvance.ai.light.model

import com.ledvance.ai.light.ui.ISceneItem
import com.ledvance.tuya.beans.ArmLightEffect
import com.ledvance.tuya.beans.ArmScene
import com.ledvance.ui.R

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 10:08
 * Describe : Scenes
 */
enum class Scene(override val title: String, override val iconResId: Int, override val id: Int) :
    ISceneItem {
    Romantic("Romantic", R.drawable.icon_scenes_rose, ArmScene.Romantic.value),
    Reading("Reading", R.drawable.icon_scenes_book, ArmScene.Reading.value),
    Relax("Relax", R.drawable.icon_scenes_palm_tree, ArmScene.Relax.value),
    Party("Party", R.drawable.icon_scenes_tada, ArmScene.Party.value),
    Sunrise("Sunrise", R.drawable.icon_scenes_sunrise, ArmScene.Sunrise.value),
    Rainbow("Rainbow", R.drawable.icon_scenes_rainbow, ArmScene.Rainbow.value),
    EyeCare("Eye Care", R.drawable.icon_scenes_eyes, ArmLightEffect.EyeCare.value),
    ColorMood("Color Mood", R.drawable.icon_scenes_milky_way, ArmLightEffect.ColorMood.value),
    FocusReminder(
        "Focus Reminder",
        R.drawable.icon_scenes_alarm_clock,
        ArmLightEffect.FocusReminder.value
    ),
    ;

    companion object Companion {
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