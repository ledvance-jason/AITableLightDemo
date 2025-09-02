package com.ledvance.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.isUnspecified
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ledvance.ui.theme.AppTheme
import com.ledvance.ui.theme.LocalBackgroundTheme

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2023/11/22 16:40
 * Describe : LedvanceScreen
 */
@Composable
fun LedvanceScreen(
    modifier: Modifier = Modifier,
    backTitle: String? = null,
    title: String? = null,
    actionIconPainter: Painter? = null,
    onBackPressed: (() -> Unit)? = null,
    onActionPressed: () -> Unit = {},
    rightIconContentDescription: String? = "",
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable () -> Unit = {},
    backgroundColor: Color = Color.Unspecified,
    content: @Composable ColumnScope.() -> Unit
) {
    val color = backgroundColor.takeIf { !it.isUnspecified } ?: LocalBackgroundTheme.current.color
    val tonalElevation = LocalBackgroundTheme.current.tonalElevation
    val localFocusManager = LocalFocusManager.current
    val keyBoardState by keyboardAsState()
    LaunchedEffect(keyBoardState) {
        if (keyBoardState == Keyboard.Closed) {
            localFocusManager.clearFocus()
        }
    }
    BackHandler(onBackPressed != null) {
        onBackPressed?.invoke()
    }
    Surface(
        color = if (color == Color.Unspecified) Color.Transparent else color,
        tonalElevation = if (tonalElevation == Dp.Unspecified) 0.dp else tonalElevation,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(onTap = {
                    localFocusManager.clearFocus()
                })
            },
    ) {

        CompositionLocalProvider(LocalAbsoluteTonalElevation provides 0.dp) {
            Scaffold(
                modifier = Modifier.fillMaxSize(),
                containerColor = if (color == Color.Unspecified) Color.Transparent else color,
                bottomBar = bottomBar,
                snackbarHost = snackbarHost
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .consumeWindowInsets(padding)
                        .windowInsetsPadding(
                            WindowInsets.safeDrawing.only(
                                WindowInsetsSides.Horizontal,
                            ),
                        )
                        .then(modifier),
                ) {
                    if (backTitle != null || actionIconPainter != null) {
                        LedvanceTopLayout(
                            backTitle = backTitle,
                            onBackClick = onBackPressed,
                            rightIconPainter = actionIconPainter,
                            onRightIconClick = onActionPressed,
                            rightIconContentDescription = rightIconContentDescription
                        )
                    }
                    if (!title.isNullOrEmpty()) {
                        Text(
                            text = title,
                            style = AppTheme.typography.headlineSmall,
                            color = AppTheme.colors.primary,
                            modifier = Modifier.padding(start = 24.dp, top = 36.dp, bottom = 26.dp)
                        )
                    }
                    this.content()
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ScreenPreview() {
    LedvanceScreen(
        backTitle = "backTitle",
        actionIconPainter = rememberVectorPainter(Icons.Outlined.Add),
        bottomBar = {

        },
        snackbarHost = {

        }
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(LocalBackgroundTheme.current.color)
        )
    }
}

