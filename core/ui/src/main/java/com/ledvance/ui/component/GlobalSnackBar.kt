package com.ledvance.ui.component

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import com.ledvance.ui.theme.LocalSnackBarHostState

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/9 14:16
 * Describe : ShowGlobalSnackBar
 */
@Composable
fun rememberSnackBarState(): SnackbarHostState {
    return LocalSnackBarHostState.current
}