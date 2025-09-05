package com.ledvance.ai.light.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.ColorMatrix
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil3.compose.rememberAsyncImagePainter
import com.ledvance.tuya.ktx.isSupportSwitch
import com.ledvance.ui.R
import com.ledvance.ui.component.LedvanceSwitch
import com.ledvance.ui.extensions.debouncedClickable
import com.ledvance.ui.theme.AppTheme
import com.thingclips.smart.sdk.bean.DeviceBean
import timber.log.Timber

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/3 17:53
 * Describe : DeviceItem
 */
@Composable
fun DeviceItem(
    device: DeviceBean,
    isOnline: Boolean,
    switch: Boolean,
    onSwitchChange: (DeviceBean, Boolean) -> Unit,
    onClick: (DeviceBean) -> Unit
) {
    val colorFilter = remember {
        ColorFilter.colorMatrix(ColorMatrix().apply { setToSaturation(0F) })
    }
    Timber.tag("DeviceBean").i(
        "DeviceItem: " +
                "${device.is433SubDev()} ${device.is433Wifi()} ${device.isThreadSubDev} ${device.isInfraredSubDev}"
    )
    Card(
        elevation = CardDefaults.cardElevation(8.dp),
        colors = CardDefaults.cardColors(containerColor = AppTheme.colors.screenBackground),
        modifier = Modifier.debouncedClickable(onClick = { onClick.invoke(device) })
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(116.dp)
                .padding(start = 20.dp, end = 20.dp, top = 12.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Image(
                    painter = rememberAsyncImagePainter(device.iconUrl),
                    contentDescription = "",
                    modifier = Modifier
                        .size(40.dp),
                    colorFilter = if (!isOnline) colorFilter else null
                )
                Spacer(modifier = Modifier.weight(1f))
                if (!isOnline) {
                    Icon(
                        painter = painterResource(R.drawable.ic_wifi_off),
                        contentDescription = "offline",
                        modifier = Modifier
                            .size(24.dp),
                        tint = AppTheme.colors.offline
                    )
                } else if (device.isSupportSwitch()) {
                    LedvanceSwitch(
                        checked = switch,
                        onCheckedChange = { onSwitchChange.invoke(device, it) },
                        modifier = Modifier,
                    )
                }
            }
            Text(
                text = device.getName() ?: "",
                maxLines = 2,
                color = if (isOnline) AppTheme.colors.title else AppTheme.colors.offline,
                overflow = TextOverflow.Ellipsis,
                style = AppTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 20.dp)
            )
        }
    }
}