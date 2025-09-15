package com.ledvance.ai.light.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ledvance.ui.extensions.debouncedClickable
import com.ledvance.ui.theme.AppTheme

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/5 10:47
 * Describe : FlowRowSection
 */
interface IFlowRowSectionItem {
    val title: String
    val id: Int
}

@Composable
fun FlowRowSection(
    items: List<IFlowRowSectionItem>,
    modifier: Modifier = Modifier,
    title: String? = null,
    maxItemsInEachRow: Int = 3,
    onItemClick: (IFlowRowSectionItem) -> Unit
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
        FlowRow(
            maxItemsInEachRow = maxItemsInEachRow,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items.forEach {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(48.dp)
                        .background(
                            color = AppTheme.colors.screenBackground,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .border(
                            width = 1.dp,
                            shape = RoundedCornerShape(10.dp),
                            color = AppTheme.colors.border
                        )
                        .clip(RoundedCornerShape(10.dp))
                        .debouncedClickable(onClick = { onItemClick.invoke(it) }),
                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = it.title,
                        color = AppTheme.colors.title,
                        style = AppTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                    )
                }
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