package com.ledvance.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import com.ledvance.ui.theme.AppTheme

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/6/12 10:57
 * Describe : LoadingCard
 */


@Composable
fun LoadingCard() {
    Popup(
        alignment = Alignment.Center,
        properties = PopupProperties(
            focusable = false,
            dismissOnClickOutside = false,
            dismissOnBackPress = false
        ),
        onDismissRequest = {}
    ) {
        Card(
            shape = RoundedCornerShape(15.dp),
            elevation = CardDefaults.elevatedCardElevation(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.padding(28.dp)
            ) {
                CircularProgressIndicator(
                    color = AppTheme.colors.primary,
                    strokeWidth = 4.dp
                )
            }
        }
    }
}