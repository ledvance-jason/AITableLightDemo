package com.ledvance.ai.light.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.ledvance.tuya.beans.ArmCustomAction
import com.ledvance.ui.R
import com.ledvance.ui.component.LDVAnimatedVisibility
import com.ledvance.ui.extensions.debouncedClickable
import com.ledvance.ui.theme.AppTheme

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 10:03
 * Describe : CustomActionView
 */


@Composable
fun CustomActionView(
    selectedItem: ArmCustomAction?,
    items: List<ArmCustomAction>,
    modifier: Modifier = Modifier,
    title: String? = null,
    maxItemsInEachRow: Int = 3,
    expanded: Boolean? = null,
    onExpanded: ((Boolean) -> Unit)? = null,
    onItemClick: (ArmCustomAction) -> Unit
) {
    Column(modifier = Modifier.then(modifier)) {
        if (!title.isNullOrEmpty()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp)
                    .clickable(indication = null, interactionSource = null, onClick = {
                        onExpanded?.invoke(expanded?.let { !it } ?: true)
                    }), verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = AppTheme.colors.title,
                    style = AppTheme.typography.titleMedium,
                    modifier = Modifier
                        .weight(1f),
                )
                expanded?.also {
                    val rotate by animateFloatAsState(
                        targetValue = if (expanded) 0f else 180f,
                        animationSpec = tween(durationMillis = 100), label = ""
                    )
                    Icon(
                        painter = painterResource(id = R.drawable.ic_expand),
                        contentDescription = "Expandable Arrow",
                        modifier = Modifier
                            .size(24.dp)
                            .rotate(rotate),
                        tint = AppTheme.colors.title
                    )
                }
            }

        }
        LDVAnimatedVisibility(expanded ?: true) {
            FlowRow(
                maxItemsInEachRow = maxItemsInEachRow,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                items.forEach {
                    CustomActionItem(
                        item = it,
                        isSelected = selectedItem?.content == it.content,
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
}

@Composable
private fun CustomActionItem(
    item: ArmCustomAction,
    isSelected: Boolean,
    modifier: Modifier = Modifier,
    onItemClick: (ArmCustomAction) -> Unit
) {
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
                onItemClick.invoke(item)
            }),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Text(
            text = item.title,
            color = if (isSelected) AppTheme.colors.primary else AppTheme.colors.title,
            style = AppTheme.typography.bodyMedium,
        )
    }
}