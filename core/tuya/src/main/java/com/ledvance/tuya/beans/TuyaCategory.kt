package com.ledvance.tuya.beans

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/11 13:20
 * Describe : TuyaCategory
 */

enum class TuyaMainCategory(private val value: String, private val displayName: String) {
    Lighting("zm", "照明"),
    Plug("dgzm", "电工"),
    SmallAppliances("xjd", "小家电"),
    Sensor("jjaf", "传感"),
    Camera("sxj", "摄像机/锁"),
    IndustryAgriculture("gny", "工农业"),
    Other("qt", "其他"),
}

enum class TuyaCategory(
    private val category: String,
    private val displayName: String,
    val mainCategory: TuyaMainCategory,
) {
    Unknown("", "未知", TuyaMainCategory.Other),
    LightingRemoteControl("ykq", "照明遥控器", TuyaMainCategory.Other),

    LightSource("dj", "光源", TuyaMainCategory.Lighting),
    StripLight("dd", "灯带", TuyaMainCategory.Lighting),
    MoodStripLight("hcdd", "幻彩灯带", TuyaMainCategory.Lighting),
    StringLight("dc", "灯串", TuyaMainCategory.Lighting),
    PIRLight("gyd", "感应灯", TuyaMainCategory.Lighting),
    CeilingLight("xdd", "吸顶灯", TuyaMainCategory.Lighting),
    CeilingFan("fsd", "风扇灯", TuyaMainCategory.Lighting),
    SolarLight("tyndj", "太阳能灯", TuyaMainCategory.Lighting),

    WifiPlug("cz", "插座", TuyaMainCategory.Plug),
    PowerStrip("pc", "排插", TuyaMainCategory.Plug),
    NightlightPlug("qjdcz", "情景灯插座", TuyaMainCategory.Plug),
    WirelessSwitch("wxkg", "无线开关", TuyaMainCategory.Plug),
    Breaker("tdq", "通断器", TuyaMainCategory.Plug),
    MatterBreaker("kg", "开关", TuyaMainCategory.Plug),

    MotionSensor("pir", "人体传感器", TuyaMainCategory.Sensor),
    ContactSensor("msc", "门磁", TuyaMainCategory.Sensor),
    WaterDetector("sj", "水浸传感器", TuyaMainCategory.Sensor),
    SmokeDetector("ywbj", "烟雾报警器", TuyaMainCategory.Sensor),
    CODetector("cobj", "CO报警器", TuyaMainCategory.Sensor),

    SmartCamera("sp", "智能摄像机", TuyaMainCategory.Camera),
    VideoDoorbell("ksdjml", "可视对讲门铃", TuyaMainCategory.Camera),
    LECamera("dghsxj", "低功耗相机", TuyaMainCategory.Camera),

    IrrigationController("sfkzq", "水阀控制器", TuyaMainCategory.IndustryAgriculture),

    ;

    companion object {
        fun fromDeviceCategory(deviceCategory: String?): TuyaCategory {
            return entries.find { it.category == deviceCategory } ?: Unknown
        }
    }
}