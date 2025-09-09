package com.ledvance.ai.light.screen

import android.Manifest
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil3.compose.rememberAsyncImagePainter
import com.dotlottie.dlplayer.Mode
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ledvance.ai.light.viewmodel.AddDevicesViewModel
import com.ledvance.tuya.beans.DeviceNetworkType
import com.ledvance.tuya.beans.ScanDevice
import com.ledvance.ui.R
import com.ledvance.ui.component.LedvanceButton
import com.ledvance.ui.component.LedvanceScreen
import com.ledvance.ui.theme.AppTheme
import com.lottiefiles.dotlottie.core.compose.ui.DotLottieAnimation
import com.lottiefiles.dotlottie.core.util.DotLottieSource
import timber.log.Timber

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/9 15:40
 * Describe : AddDeviceScreen
 */

private val requiredBtPermissions: List<String>
    get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        listOf(
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
    } else {
        listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH
        )
    }

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun AddDevicesScreen(viewModel: AddDevicesViewModel = hiltViewModel(), onBackPressed: () -> Unit) {
    val scanDevices by viewModel.scanDevicesFlow.collectAsStateWithLifecycle()
    var startSearch by remember { mutableStateOf(false) }
    val cameraPermissionState = rememberMultiplePermissionsState(requiredBtPermissions)
    val firstCameraPermissionState by remember {
        mutableStateOf(cameraPermissionState.allPermissionsGranted)
    }

    LaunchedEffect(cameraPermissionState.allPermissionsGranted) {
        val isGranted = cameraPermissionState.allPermissionsGranted
        if (firstCameraPermissionState != isGranted && isGranted) {
            viewModel.startScanDevice()
            startSearch = true
        }
    }

    LedvanceScreen(
        backTitle = "Home",
        title = "Add devices",
        onBackPressed = {
            Timber.tag("TAG").i("AddDevicesScreen: onBackPressed")
            onBackPressed.invoke()
        },
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        when {
            !startSearch -> {
                LedvanceButton(text = "Start automatic search") {
                    if (cameraPermissionState.allPermissionsGranted) {
                        viewModel.startScanDevice()
                        startSearch = true
                    } else {
                        cameraPermissionState.launchMultiplePermissionRequest()
                    }
                }
            }

            scanDevices.isEmpty() -> {
                DotLottieAnimation(
                    source = DotLottieSource.Asset("searching.lottie"),
                    autoplay = true,
                    loop = true,
                    speed = 2f,
                    useFrameInterpolation = false,
                    playMode = Mode.FORWARD,
                    modifier = Modifier
                        .size(300.dp)
                        .align(Alignment.TopCenter)
                )
            }

            else -> LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 10.dp)
            ) {
                items(scanDevices) {
                    ScanDeviceCard(it)
                }
            }
        }
    }
}

@Composable
private fun ScanDeviceCard(scanDevice: ScanDevice) {
    Card(
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.elevatedCardElevation(8.dp),
        shape = RoundedCornerShape(6.dp),
        modifier = Modifier.padding(vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp, vertical = 15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Image(
                painter = rememberAsyncImagePainter(
                    model = scanDevice.icon,
                    error = painterResource(R.drawable.ic_lamp),
                    placeholder = painterResource(R.drawable.ic_lamp),
                    contentScale = ContentScale.FillWidth
                ),
                contentDescription = null,
                modifier = Modifier.size(24.dp)
            )
            val iconRes = when (scanDevice.networkType) {
                DeviceNetworkType.Ble -> R.drawable.ic_ble
                DeviceNetworkType.Wifi -> R.drawable.ic_wifi
                DeviceNetworkType.Camera -> R.drawable.ic_wifi
                DeviceNetworkType.SigMesh -> R.drawable.ic_ble
                DeviceNetworkType.Matter -> R.drawable.ic_matter
                DeviceNetworkType.TuyaPureBt -> R.drawable.ic_ble
            }
            Icon(
                painter = painterResource(iconRes),
                contentDescription = "",
                modifier = Modifier
                    .padding(start = 8.dp)
                    .size(16.dp),
                tint = AppTheme.colors.title
            )
            Text(
                text = scanDevice.name,
                color = AppTheme.colors.title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = AppTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .weight(1f)
            )

            Card(
                shape = RoundedCornerShape(4.dp),
                colors = CardDefaults.cardColors(
                    containerColor = AppTheme.colors.buttonBackground,
                    contentColor = AppTheme.colors.buttonContent
                ),
                elevation = CardDefaults.elevatedCardElevation(4.dp),
                modifier = Modifier.size(width = 40.dp, height = 24.dp),
                onClick = {}) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Add",
                        fontSize = 12.sp
                    )
                }
            }
        }

    }
}