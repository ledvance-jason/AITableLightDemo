package com.ledvance.ai.light.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.Lifecycle
import com.ledvance.ui.R
import com.ledvance.ui.extensions.ComposeLifecycleEvent
import com.ledvance.ui.extensions.debouncedClickable
import com.ledvance.ui.theme.AppTheme
import com.ledvance.utils.extensions.openWifiSettingsPage
import com.ledvance.utils.extensions.wifiSsid

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/9 15:43
 * Describe : SelectWiFiNetworkScreen
 */
@Composable
fun SelectWiFiNetworkScreen(onDismissRequest: () -> Unit, onConfirm: (String, String) -> Unit) {
    val context = LocalContext.current
    var isNeedUpdateSsid by remember { mutableStateOf(false) }
    var wifiName by remember {
        mutableStateOf(context.wifiSsid)
    }
    var password by remember { mutableStateOf("") }

    ComposeLifecycleEvent {
        if (it == Lifecycle.Event.ON_RESUME && isNeedUpdateSsid) {
            isNeedUpdateSsid = false
            wifiName = context.wifiSsid
        }
    }
    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = false,
            dismissOnClickOutside = false,
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(horizontal = 24.dp, vertical = 50.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Select WiFi Network",
                style = AppTheme.typography.headlineSmall,
                color = AppTheme.colors.primary,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 36.dp)
            )
            Text(
                text = "Do you want to use the following WiFi network for adding your WiFi devices?",
                modifier = Modifier.padding(top = 20.dp),
                color = AppTheme.colors.title,
                style = AppTheme.typography.titleMedium.copy(
                    fontSize = 16.sp,
                    lineHeight = 20.sp
                )
            )

            OutlinedTextField(
                value = wifiName,
                onValueChange = { wifiName = it },
                label = { Text("Wi-Fi name") },
                placeholder = { Text("Wi-Fi name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                placeholder = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 20.dp)
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 20.dp)
                    .debouncedClickable(onClick = {
                        isNeedUpdateSsid = true
                        context.openWifiSettingsPage()
                    }),
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_wifi),
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = AppTheme.colors.primary
                )
                Text(
                    text = "Change WiFi network",
                    color = AppTheme.colors.primary,
                    style = AppTheme.typography.bodyMedium,
                    modifier = Modifier.padding(start = 5.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Row(modifier = Modifier.fillMaxWidth()) {
                Icon(
                    painter = painterResource(R.drawable.ic_caption),
                    contentDescription = null,
                    modifier = Modifier
                        .offset(y = 4.dp)
                        .size(16.dp),
                    tint = AppTheme.colors.body
                )
                Text(
                    text = "If your WiFi network is set at 5GHz, please set it to be 2.4GHz.",
                    modifier = Modifier.padding(bottom = 20.dp, start = 2.dp),
                    color = AppTheme.colors.body,
                    fontSize = 14.sp
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "CANCEL",
                    style = AppTheme.typography.titleMedium,
                    color = AppTheme.colors.primary,
                    modifier = Modifier.debouncedClickable(onClick = onDismissRequest)
                )
                Text(
                    text = "CONFIRM",
                    style = AppTheme.typography.titleMedium,
                    color = AppTheme.colors.primary,
                    modifier = Modifier
                        .padding(start = 20.dp)
                        .debouncedClickable(onClick = {
                            onConfirm.invoke(wifiName, password)
                            onDismissRequest.invoke()
                        })
                )
            }
        }
    }
}