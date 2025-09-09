package com.ledvance.ui.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.ledvance.ui.R
import com.ledvance.ui.extensions.debouncedClickable
import com.ledvance.ui.theme.AppTheme

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/6/4 08:45
 * Describe : LedvanceTitleLayout
 */

@Composable
fun LedvanceTopLayout(
    backTitle: String? = null,
    onBackClick: (() -> Unit)? = null,
    rightIconPainter: Painter? = null,
    rightIconContentDescription: String? = null,
    onRightIconClick: (() -> Unit)? = null
) {
    val onBack by rememberUpdatedState(onBackClick)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(46.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (backTitle != null) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxHeight()
                    .clip(shape = RoundedCornerShape(10.dp))
                    .debouncedClickable(onClick = { onBack?.invoke() })
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "back",
                    modifier = Modifier.size(18.dp),
                    tint = AppTheme.colors.primary
                )
                Text(
                    text = backTitle,
                    style = AppTheme.typography.bodyLarge,
                    maxLines = 1,
                    color = AppTheme.colors.primary,
                    modifier = Modifier.padding(start = 5.dp),
                    overflow = TextOverflow.Ellipsis,
                )

            }
        }
        Spacer(modifier = Modifier.weight(1f))

        if (rightIconPainter != null) {
            Icon(
                painter = rightIconPainter,
                contentDescription = rightIconContentDescription,
                tint = AppTheme.colors.primary,
                modifier = Modifier
                    .size(26.dp)
                    .debouncedClickable {
                        onRightIconClick?.invoke()
                    }
            )
        }
    }
}