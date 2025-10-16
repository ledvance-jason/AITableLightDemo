package com.ledvance.tuya.beans

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/15 14:47
 * Describe : ArmCustomAction
 */
enum class ArmCustomAction(val title: String, val content: String) {
    CatNuzzle("Cat Nuzzle", "cengceng"),
    PowerOff("Power Off", "daiji"),
    Nod("Nod", "diantou"),
    WakeUp("Wake Up", "huanxing"),
    Surprised("Surprised", "jingkong"),
    StartupGuide("Startup Guide", "kaiji"),
    Happy("Happy", "kaixin"),
    EmotionMode("Emotion Mode", "qingxv"),
    Angry("Angry", "shengqi"),
    Sad("Sad", "shiluo"),
    HandTracking("Hand Tracking", "shoubu"),
    DanceHappy("Dance(Happy)", "wudao01"),
    DanceRelaxing("Dance(Relaxing)", "wudao02"),
    StudyMode("Study Mode", "xuexi"),
    CatHeadTilt("Cat Head Tilt", "yangtou"),
    ;

    companion object {

        val allAction = listOf(
            CatNuzzle,
            PowerOff,
            Nod,
            WakeUp,
            Surprised,
            StartupGuide,
            Happy,
            EmotionMode,
            Angry,
            Sad,
            HandTracking,
            DanceHappy,
            DanceRelaxing,
            StudyMode,
            CatHeadTilt
        )

        fun of(content: String): ArmCustomAction? {
            return entries.find { it.content == content }
        }
    }
}