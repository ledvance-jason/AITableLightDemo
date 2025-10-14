package com.ledvance.tuya.beans

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/15 14:48
 * Describe : ArmLightEffect
 */
enum class ArmLightEffect(val value: Int) {
    Unknown(0),
    EyeCare(1),
    EmotionMode(2),
    StudyMode(3),
    StartupGuide(4),
    FocusReminder(5),
    CatNuzzle(6),             // 猫咪蹭一蹭（左右）
    CatHeadTilt(7),           // 猫咪仰头（上下）
    Happy(8),                 // 开心（情绪识别）
    Angry(9),                 // 生气（情绪识别）
    Calm(10),                  // 平静（情绪识别）
    Surprised(11),             // 惊恐（情绪识别）
    Sad(12),                   // 失落（语音识别）
    HandTracking(13),     // 手部追踪模式进入动作
    DanceHappy(14),           // 舞蹈动作组1（开心）
    DanceRelaxing(15),           // 舞蹈动作组2（舒缓）
    Nod(16),                   // 点头（认可）
    ShakeHead(17),            // 摇头（否定）
    WakeUp(18),            // 唤醒
    PowerOff(19),             // 关机
    DailyMode(20),           // 日常模式
    ;

    companion object {
        fun of(value: Int): ArmLightEffect {
            return entries.find { it.value == value } ?: Unknown
        }
    }
}