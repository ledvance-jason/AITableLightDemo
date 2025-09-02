package com.ledvance.ai.light

import android.app.Application
import android.content.Context
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import coil3.request.crossfade
import com.ledvance.utils.AppContext
import com.ledvance.utils.extensions.enableStrictModePolicy
import com.ledvance.utils.extensions.enableTimerDebugTree
import dagger.hilt.android.HiltAndroidApp

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/2 10:17
 * Describe : LdvApplication
 */
@HiltAndroidApp
class LdvApplication : Application(), SingletonImageLoader.Factory {
    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        AppContext.init(this)
        enableTimerDebugTree()
    }

    override fun onCreate() {
        super.onCreate()
        enableStrictModePolicy()
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return ImageLoader.Builder(context)
            .crossfade(true)
            .build()
    }
}