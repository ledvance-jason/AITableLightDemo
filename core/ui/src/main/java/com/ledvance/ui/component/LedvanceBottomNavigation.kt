package com.ledvance.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ledvance.ui.extensions.debouncedClickable
import com.ledvance.ui.theme.AppTheme
import kotlinx.serialization.Serializable

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/2 16:23
 * Describe : LedvanceBottomNavigation
 */
@Serializable
data class LedvanceBottomItem<T>(
    val iconResId: Int,
    val titleResId: Int,
    val data: T
)

@Composable
fun <T> LedvanceBottomNavigation(
    selectedIndex: Int = 0,
    items: List<LedvanceBottomItem<T>>,
    selectedColor: Color = AppTheme.colors.primary,
    normalColor: Color = AppTheme.colors.body,
    onClick: (Int, LedvanceBottomItem<T>) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp)
            .height(48.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items.forEachIndexed { index, item ->
            BottomBarItem(
                item = item,
                color = if (index == selectedIndex) selectedColor else normalColor,
                modifier = Modifier
                    .weight(1f)
                    .debouncedClickable(onClick = {
                        onClick.invoke(index, item)
                    })
            )
        }
    }
}

@Composable
private fun <T> BottomBarItem(
    item: LedvanceBottomItem<T>,
    color: Color,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(item.iconResId),
            contentDescription = stringResource(item.titleResId),
            colorFilter = ColorFilter.tint(color),
            contentScale = ContentScale.FillWidth,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = stringResource(item.titleResId),
            color = color,
            textAlign = TextAlign.Center,
            fontSize = 12.sp,
        )
    }
}