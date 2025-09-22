package com.ledvance.ai.light.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledvance.ai.light.test.BuildConfig
import com.ledvance.ai.light.utils.DataStoreKeys
import com.ledvance.log.LogManager
import com.ledvance.ui.component.LedvanceButton
import com.ledvance.ui.component.LedvanceScreen
import com.ledvance.ui.component.LedvanceSwitch
import com.ledvance.ui.component.LoadingCard
import com.ledvance.ui.theme.AppTheme
import com.ledvance.utils.extensions.getBoolean
import com.ledvance.utils.extensions.setBoolean
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/8 09:35
 * Describe : TestModeScreen
 */
@Composable
fun TestModeScreen(onBackPressed: (() -> Unit)? = null) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    val enableDeviceDeleteButton by DataStoreKeys.enableDeviceDeleteButton.getBoolean()
        .map { it ?: false }
        .collectAsStateWithLifecycle(false)
    var loading by remember { mutableStateOf(false) }
    if (loading) {
        LoadingCard()
    }
    LedvanceScreen(
        backTitle = "Home",
        title = "Test Mode",
        onBackPressed = onBackPressed,
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Display device delete button",
                    color = AppTheme.colors.title,
                    style = AppTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.weight(1f))
                LedvanceSwitch(
                    checked = enableDeviceDeleteButton,
                    onCheckedChange = {
                        scope.launch {
                            DataStoreKeys.enableDeviceDeleteButton.setBoolean(it)
                        }
                    },
                    modifier = Modifier,
                )
            }
            LedvanceButton(text = "Share App Logs", modifier = Modifier.padding(top = 20.dp)) {
                scope.launch {
                    loading = true
                    LogManager.shareAppLog(context)
                    loading = false
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Version : v${BuildConfig.VERSION_NAME}(${BuildConfig.BUILD_TYPE})",
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.body
            )
            Text(
                text = "Build time: ${BuildConfig.BUILD_TIME}",
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.body,
                modifier = Modifier.padding(vertical = 10.dp)
            )
        }
    }
}