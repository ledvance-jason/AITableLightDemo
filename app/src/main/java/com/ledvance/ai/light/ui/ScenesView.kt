package com.ledvance.ai.light.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ledvance.ui.extensions.debouncedClickable
import com.ledvance.ui.theme.AppTheme

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 10:03
 * Describe : ScenesView
 */
interface ISceneItem {
    val title: String
    val iconResId: Int
    val id: Int
}

@Composable
fun ScenesView(
    items: List<ISceneItem>,
    modifier: Modifier = Modifier,
    title: String? = null,
    maxItemsInEachRow: Int = 3,
    onItemClick: (ISceneItem) -> Unit
) {
    Column(modifier = Modifier.then(modifier)) {
        if (!title.isNullOrEmpty()) {
            Text(
                text = title,
                color = AppTheme.colors.title,
                style = AppTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 15.dp),
            )
        }
        FlowRow(
            maxItemsInEachRow = maxItemsInEachRow,
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach {
                ScenesItem(
                    scenes = it,
                    modifier = Modifier
                        .weight(1f)
                        .debouncedClickable(onClick = {
                            onItemClick.invoke(it)
                        })
                )
            }
            val count = (items.size % maxItemsInEachRow)
            if (count > 0) {
                repeat(maxItemsInEachRow - count) {
                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }
}

@Composable
private fun ScenesItem(scenes: ISceneItem, modifier: Modifier = Modifier) {
    Row(
        modifier = Modifier
            .height(48.dp)
            .then(modifier)
            .background(
                color = AppTheme.colors.screenBackground,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = AppTheme.colors.border,
                shape = RoundedCornerShape(10.dp)
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Image(
            painter = painterResource(scenes.iconResId),
            contentDescription = scenes.title,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .padding(start = 10.dp, top = 5.dp)
                .width(24.dp)
        )
        Text(
            text = scenes.title,
            color = AppTheme.colors.title,
            style = AppTheme.typography.bodyMedium,
            modifier = Modifier.padding(start = 5.dp)
        )
    }
}