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
    selectedItem: ISceneItem? = null,
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
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items.forEach {
                ScenesItem(
                    scenes = it,
                    isSelected = selectedItem?.id == it.id,
                    modifier = Modifier
                        .weight(1f),
                    onItemClick = onItemClick
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
private fun ScenesItem(
    scenes: ISceneItem,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onItemClick: (ISceneItem) -> Unit
) {
    val isShowIcon = scenes.iconResId > 0
    Row(
        modifier = Modifier
            .height(48.dp)
            .then(modifier)
            .background(
                color = if (isSelected) Color(0xFFFFF4E5) else AppTheme.colors.screenBackground,
                shape = RoundedCornerShape(10.dp)
            )
            .border(
                width = 1.dp,
                color = if (isSelected) Color(0xFFFF7A00) else AppTheme.colors.border,
                shape = RoundedCornerShape(10.dp)
            )
            .clip(RoundedCornerShape(10.dp))
            .debouncedClickable(onClick = {
                onItemClick.invoke(scenes)
            }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = if (isShowIcon) Arrangement.Start else Arrangement.Center
    ) {
        if (isShowIcon) {
            Image(
                painter = painterResource(scenes.iconResId),
                contentDescription = scenes.title,
                contentScale = ContentScale.FillWidth,
                modifier = Modifier
                    .padding(start = 5.dp, top = 5.dp, end = 5.dp)
                    .width(24.dp)
            )
        }
        Text(
            text = scenes.title,
            color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.title,
            style = AppTheme.typography.bodyMedium,
        )
    }
}