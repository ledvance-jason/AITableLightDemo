package com.ledvance.ai.light.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ledvance.log.LogManager
import com.ledvance.ui.component.LedvanceButton
import com.ledvance.ui.component.LedvanceScreen
import com.ledvance.ui.component.LoadingCard
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

        LedvanceButton("Share App Logs") {
            scope.launch {
                loading = true
                LogManager.shareAppLog(context)
                loading = false
            }
        }
    }
}