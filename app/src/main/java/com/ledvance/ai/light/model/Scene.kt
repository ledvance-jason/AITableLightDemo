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
    EyeCare("Eye Care", 0, ArmLightEffect.EyeCare.value),
    ColorMood("Color Mood", 0, ArmLightEffect.ColorMood.value),
    FocusReminder(
        "Focus Reminder",
        0,
        ArmLightEffect.FocusReminder.value
    ),

    CatNuzzle(
        "Cat Nuzzle",
        0,
        ArmLightEffect.CatNuzzle.value
    ),
    CatHeadTilt(
        "Cat Head Tilt",
        0,
        ArmLightEffect.CatHeadTilt.value
    ),
    Happy(
        "Happy",
        0,
        ArmLightEffect.Happy.value
    ),
    Angry(
        "Angry",
        0,
        ArmLightEffect.Angry.value
    ),
    Calm(
        "Calm",
        0,
        ArmLightEffect.Calm.value
    ),
    Surprised(
        "Surprised",
        0,
        ArmLightEffect.Surprised.value
    ),
    Sad(
        "Sad",
        0,
        ArmLightEffect.Sad.value
    ),
    HandTracking(
        "Hand Tracking",
        0,
        ArmLightEffect.HandTracking.value
    ),
    DanceHappy(
        "Dance(Happy)",
        0,
        ArmLightEffect.DanceHappy.value
    ),
    DanceRelaxing(
        "Dance(Relaxing)",
        0,
        ArmLightEffect.DanceRelaxing.value
    ),
    Nod(
        "Nod",
        0,
        ArmLightEffect.Nod.value
    ),
    ShakeHead(
        "Shake Head",
        0,
        ArmLightEffect.ShakeHead.value
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
            CatNuzzle,
            CatHeadTilt,
            Happy,
            Angry,
            Calm,
            Surprised,
            Sad,
            HandTracking,
            DanceHappy,
            DanceRelaxing,
            Nod,
            ShakeHead,
        )
    }
}