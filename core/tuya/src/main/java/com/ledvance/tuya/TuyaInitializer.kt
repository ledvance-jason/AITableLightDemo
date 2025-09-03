package com.ledvance.tuya

import android.content.Context
import androidx.startup.Initializer
import com.ledvance.utils.AppContext
import com.thingclips.smart.home.sdk.ThingHomeSdk

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/3 09:25
 * Describe : TuyaInitializer
 */
internal class TuyaInitializer : Initializer<Boolean> {
    override fun create(context: Context): Boolean {
        TuyaSdkManager.init(AppContext.get())
        return true
    }

    override fun dependencies(): List<Class<out Initializer<*>?>?> {
        return emptyList()
    }
}