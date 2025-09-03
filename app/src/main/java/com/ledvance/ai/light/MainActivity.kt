package com.ledvance.ai.light

import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.util.trace
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ledvance.ai.light.model.DarkThemeMode
import com.ledvance.ai.light.navigation.MainNavigation
import com.ledvance.ai.light.utils.DataStoreKeys
import com.ledvance.ui.theme.LedvanceTheme
import com.ledvance.utils.extensions.getInt
import com.ledvance.utils.extensions.isSystemInDarkTheme
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        var darkTheme by mutableStateOf(resources.configuration.isSystemInDarkTheme)
        initDarkTheme(onDarkThemeChanged = { isDarkTheme ->
            darkTheme = isDarkTheme
        })
        setContent {
            LedvanceTheme(darkTheme = darkTheme) {
                MainNavigation()
            }
        }
    }

    private fun initDarkTheme(onDarkThemeChanged: (Boolean) -> Unit) {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                combine(
                    isSystemInDarkTheme(),
                    DataStoreKeys.darkThemeMode.getInt()
                ) { isSystemInDarkTheme, darkThemeMode ->
                    Timber.i("isSystemInDarkTheme:$isSystemInDarkTheme,darkThemeMode:$darkThemeMode")
                    return@combine when (DarkThemeMode.valueOf(darkThemeMode)) {
                        DarkThemeMode.Dark -> true
                        DarkThemeMode.FollowSystem -> isSystemInDarkTheme
                        DarkThemeMode.Light -> false
                    }
                }
                    .distinctUntilChanged()
                    .onEach { onDarkThemeChanged.invoke(it) }
                    .collectLatest { darkTheme ->
                        trace("LedvanceEdgeToEdge") {
                            enableEdgeToEdge(
                                statusBarStyle = SystemBarStyle.auto(
                                    lightScrim = Color.TRANSPARENT,
                                    darkScrim = Color.TRANSPARENT
                                ) { darkTheme },
                                navigationBarStyle = SystemBarStyle.auto(
                                    lightScrim = lightScrim,
                                    darkScrim = darkScrim
                                ) { darkTheme })
                        }
                    }
            }
        }
    }

    companion object {
        private const val TAG = "MainActivity"

        /**
         * The default light scrim, as defined by androidx and the platform:
         * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=35-38;drc=27e7d52e8604a080133e8b842db10c89b4482598
         */
        private val lightScrim = Color.argb(0xe6, 0xFF, 0xFF, 0xFF)

        /**
         * The default dark scrim, as defined by androidx and the platform:
         * https://cs.android.com/androidx/platform/frameworks/support/+/androidx-main:activity/activity/src/main/java/androidx/activity/EdgeToEdge.kt;l=40-44;drc=27e7d52e8604a080133e8b842db10c89b4482598
         */
        private val darkScrim = Color.argb(0x80, 0x1b, 0x1b, 0x1b)
    }
}

