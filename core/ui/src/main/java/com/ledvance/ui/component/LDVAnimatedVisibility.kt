package com.ledvance.ui.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.runtime.Composable

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/5/30 14:26
 * Describe : LDVAnimatedVisibility
 */
@Composable
fun LDVAnimatedVisibility(
    visible: Boolean,
    content: @Composable ColumnScope.() -> Unit
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(delayMillis = 200)),
        exit = fadeOut(snap()),
    ){
        Column(content = content)
    }
}