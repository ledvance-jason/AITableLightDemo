package com.ledvance.ui.extensions

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/6/18 14:58
 * Describe : UIExtensions
 */
@Composable
fun Dp.toPx(): Float {
    val density = LocalDensity.current
    return with(density) { this@toPx.toPx() }
}

@Composable
fun Int.toDp(): Dp {
    val density = LocalDensity.current
    return with(density) { this@toDp.toDp() }
}


@Composable
fun stringResourceFormat(@StringRes id: Int, vararg formatArgs: Any): String {
    val text = stringResource(id)
    return remember(id, *formatArgs) {
        var replacedText = text
        formatArgs.forEachIndexed { index, _ ->
            val placeholder = "[[placeholder${index + 1}]]"
            val formatStr = "%${index + 1}\$s"
            replacedText = replacedText.replace(placeholder, formatStr)
        }
        replacedText.format(*formatArgs)
    }
}
