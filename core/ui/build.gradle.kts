plugins {
    alias(libs.plugins.ledvance.android.library)
    alias(libs.plugins.ledvance.android.library.compose)
}

android {
    namespace = "com.ledvance.ui"
  }

dependencies {
    implementation(libs.metrics.performance)
    implementation(libs.activity.compose)
    implementation(libs.timber)
    implementation(libs.coil.compose)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui.compose)
}