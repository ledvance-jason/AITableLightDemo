package com.ledvance.ai.light.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ledvance.ui.extensions.debouncedClickable
import com.ledvance.ui.theme.AppTheme

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 16:39
 * Describe : ModeView
 */
interface IModeItem {
    val title: String
    val iconResId: Int
    val id: Int
}

@Composable
fun ModeView(
    items: List<IModeItem>,
    modifier: Modifier = Modifier,
    title: String? = null,
    onItemClick: (IModeItem) -> Unit
) {
    Column(modifier = Modifier.then(modifier)) {
        if (!title.isNullOrEmpty()) {
            Text(
                text = title,
                color = AppTheme.colors.title,
                style = AppTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 15.dp)
            )
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            items.forEachIndexed { index, item ->
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .aspectRatio(1f)
                        .clip(shape = CircleShape)
                        .debouncedClickable(onClick = { onItemClick.invoke(item) }),
                    contentAlignment = Alignment.Center
                ) {

                    Image(
                        painter = painterResource(item.iconResId),
                        contentDescription = null,
                        modifier = Modifier.fillMaxWidth(),
                        contentScale = ContentScale.Crop
                    )
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Black.copy(alpha = 0.3f))
                    )
                    Text(
                        text = item.title,
                        color = Color.White,
                        style = AppTheme.typography.titleMedium,
                    )
                }
                if (index != items.lastIndex) {
                    Spacer(modifier = Modifier.width(15.dp))
                }

            }
        }
    }
}