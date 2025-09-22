import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

plugins {
    alias(libs.plugins.ledvance.android.application)
    alias(libs.plugins.ledvance.android.application.compose)
    alias(libs.plugins.ledvance.application.signing)
    alias(libs.plugins.ledvance.android.hilt)
    alias(libs.plugins.ledvance.kotlinx.serialization)
}

android {
    val useApplicationId = providers.gradleProperty("applicationId").orNull ?: ""
    namespace = useApplicationId
    defaultConfig {
        applicationId = useApplicationId
        versionCode = providers.gradleProperty("versionCode").orNull?.toIntOrNull() ?: 0
        versionName = providers.gradleProperty("versionName").orNull ?: ""
        setProperty("archivesBaseName", "LdvAITableLight-$versionName[$versionCode]_${getDate()}")
        val buildTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .format(LocalDateTime.now())
        buildConfigField("String", "BUILD_TIME", "\"${buildTime}\"")
        androidResources {
            val language = listOf("en")
            localeFilters.addAll(language)
        }
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        jniLibs {
            pickFirsts.add("lib/*/libc++_shared.so")
        }
    }
}

dependencies {
    implementation(projects.core.ui)
    implementation(projects.core.utils)
    implementation(projects.core.tuya)
    implementation(projects.core.log)

    implementation(libs.appcompat)
    implementation(libs.core.ktx)
    implementation(libs.activity.compose)
    implementation(libs.splashscreen)
    implementation(libs.datastore)
    implementation(libs.startup.runtime)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.accompanist.permissions)
    implementation(libs.dotlottie)

    implementation(libs.navigation3.runtime)
    implementation(libs.navigation3.ui)
    implementation(libs.lifecycle.viewmodel.navigation3)

    implementation(libs.hilt.navigation.compose)
    implementation(libs.timber)
    implementation(libs.tracing.ktx)
    implementation(libs.metrics.performance)
    implementation(libs.coil.compose)
    implementation(libs.coil.okhttp)
    implementation(libs.fastjson)
}

fun getDate(): String {
    val dateFormatter = DateTimeFormatter.ofPattern("yyMMddHH")
    return dateFormatter.format(LocalDateTime.now())
}