package com.ledvance.ui.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ledvance.ui.theme.AppTheme

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/6/2 10:05
 * Describe : LedvanceButton
 */
@Composable
fun LedvanceButton(
    text: String,
    modifier: Modifier = Modifier,
    containerColor: Color = AppTheme.colors.buttonBackground,
    contentColor: Color = AppTheme.colors.buttonContent,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .then(modifier)
            .height(46.dp)
    ) {
        Button(
            modifier = Modifier.fillMaxSize(),
            onClick = onClick,
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.filledTonalButtonColors(
                containerColor = containerColor,
                contentColor = contentColor,
            )
        ) {
            Text(
                text = text,
                maxLines = 1,
                style = AppTheme.typography.titleSmall,
                fontSize = 16.sp,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}