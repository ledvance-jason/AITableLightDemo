package com.ledvance.ai.light.initializer

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.startup.Initializer
import com.ledvance.ai.light.model.DarkThemeMode
import com.ledvance.ai.light.utils.DataStoreKeys
import com.ledvance.utils.CoroutineScopeUtils
import com.ledvance.utils.extensions.getInt
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import timber.log.Timber

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/6/19 08:36
 * Describe : DarkThemeInitializer
 */
class DarkThemeInitializer : Initializer<Boolean> {
    private val TAG = "DarkThemeInitializer"

    override fun create(context: Context): Boolean {
        CoroutineScopeUtils.launch {
            DataStoreKeys.darkThemeMode.getInt().distinctUntilChanged().map {
                DarkThemeMode.valueOf(it)
            }.collectLatest { darkThemeMode ->
                val nightMode = when (darkThemeMode) {
                    DarkThemeMode.Dark -> AppCompatDelegate.MODE_NIGHT_YES
                    DarkThemeMode.FollowSystem -> AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    DarkThemeMode.Light -> AppCompatDelegate.MODE_NIGHT_NO
                }
                Timber.tag(TAG).i("darkThemeMode->$darkThemeMode,nightMode->$nightMode")
                AppCompatDelegate.setDefaultNightMode(nightMode)
            }
        }
        return true
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}