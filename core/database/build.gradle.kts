plugins {
    alias(libs.plugins.ledvance.android.library)
    alias(libs.plugins.ledvance.android.room)
    alias(libs.plugins.ledvance.android.hilt)
}

android {
    namespace = "com.ledvance.database"
}