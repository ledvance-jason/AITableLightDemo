import java.util.Properties

plugins {
    alias(libs.plugins.ledvance.android.library)
    alias(libs.plugins.ledvance.kotlinx.serialization)
    alias(libs.plugins.ledvance.android.hilt)
}

android {
    namespace = "com.ledvance.tuya"
    defaultConfig {
        val properties = Properties().apply {
            load(rootProject.file("local.properties").inputStream())
        }
        manifestPlaceholders.putAll(
            mapOf(
                "TUYA_SMART_APPKEY" to properties.getProperty("tuyaAppKey"),
                "TUYA_SMART_SECRET" to properties.getProperty("tuyaAppSecret"),
                "INTERNAL_HOST" to "${properties.getProperty("tuyaScheme")}.app.tuya.com",
            )
        )
    }
}

configurations.all {
    exclude(group = "com.thingclips.smart", module = "thingplugin-annotation")
    exclude(group = "com.thingclips.android.module", module = "thingmodule-annotation")
    exclude(group = "com.thingclips.smart", module = "thingsmart-modularCampAnno")
    exclude(group = "com.thingclips.smart", module = "thingsmart-geofence-huawei")
    exclude(group = "com.umeng.umsdk", module = "huawei-basetb")
    exclude(group = "com.squareup.okhttp3", module = "okhttp-jvm")
    exclude(group = "commons-io", module = "commons-io")
}

dependencies {
    implementation(projects.core.utils)
    implementation(projects.aars.tuya)

    api(libs.thingsmart)
    implementation(platform(libs.thingsmart.bizbundle.bom))
    implementation(libs.thingsmart.bizbundle.family)
    implementation(libs.thingsmart.bizbundle.deviceactivator)
    implementation(libs.thingsmart.bizbundle.qrcode)
    implementation(libs.fastjson)
    implementation(libs.okhttp3.urlconnection)
    implementation(libs.startup.runtime)
    implementation(libs.timber)
}