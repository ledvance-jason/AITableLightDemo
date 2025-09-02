package com.ledvance.ai.light.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ledvance.ui.component.LedvanceButton
import com.ledvance.ui.component.LedvanceScreen
import com.ledvance.ui.component.LoadingCard
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/2 13:13
 * Describe : LoginScreen
 */
@Composable
fun LoginScreen() {
    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(false) }
    if (loading) {
        LoadingCard()
    }
    LedvanceScreen(title = "Login") {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            LedvanceButton(
                text = "One-Tap Sign In",
                modifier = Modifier.padding(horizontal = 50.dp)
            ) {
                scope.launch {
                    loading = true
                    delay(2000)
                    loading = false
                }
            }
        }
    }
}