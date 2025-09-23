package com.ledvance.ui.extensions

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/23 09:13
 * Describe : RememberExtensions
 */
@Composable
fun <T> rememberSyncedState(value: T): MutableState<T> {
    val state = remember { mutableStateOf(value) }
    LaunchedEffect(value) {
        state.value = value
    }
    return state
}