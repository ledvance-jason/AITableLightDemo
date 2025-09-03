package com.ledvance.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/3 15:53
 * Describe : InitializeScope
 */
@Composable
fun InitializeScope(block: suspend CoroutineScope.() -> Unit) {
    var initialized by rememberSaveable { mutableStateOf(false) }
    LaunchedEffect(initialized) {
        if (!initialized) {
            block()
            initialized = true
        }
    }
}