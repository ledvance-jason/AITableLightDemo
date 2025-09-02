package com.ledvance.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.ledvance.ui.extensions.debouncedClickable
import com.ledvance.ui.theme.AppTheme

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/6/2 12:04
 * Describe : CommonDialog
 */
@Composable
fun CommonDialog(
    title: String,
    content: String,
    confirmText: String,
    onDismissRequest: () -> Unit = {},
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismissRequest) {
        Column(
            modifier = Modifier.background(
                color = AppTheme.colors.dialogBackground,
                shape = RoundedCornerShape(14.dp)
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colors.dialogTitle,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .padding(horizontal = 16.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = content,
                style = AppTheme.typography.bodyMedium,
                color = AppTheme.colors.dialogMessage,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 4.dp, bottom = 20.dp)
                    .padding(horizontal = 16.dp),
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
            HorizontalDivider(color = AppTheme.colors.divider)
            Text(
                text = confirmText,
                color = AppTheme.colors.dialogPositive,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .debouncedClickable {
                        onConfirm.invoke()
                    },
                style = AppTheme.typography.titleMedium.copy(fontWeight = FontWeight.Normal)
            )
        }
    }
}