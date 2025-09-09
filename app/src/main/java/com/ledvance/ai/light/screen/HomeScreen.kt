package com.ledvance.ai.light.screen

import android.Manifest
import android.os.Build
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.ledvance.ai.light.ui.DeviceItem
import com.ledvance.ai.light.viewmodel.HomeViewModel
import com.ledvance.tuya.TuyaSdkManager
import com.ledvance.ui.R
import com.ledvance.ui.component.InitializeScope
import com.ledvance.ui.component.LedvanceButton
import com.ledvance.ui.component.LedvanceScreen
import com.ledvance.ui.component.LoadingCard
import com.ledvance.ui.component.PullRefresh
import com.ledvance.ui.component.rememberSnackBarState
import com.thingclips.smart.sdk.bean.DeviceBean
import kotlinx.coroutines.launch

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/2 17:02
 * Describe : HomeScreen
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onGotoTestMode: () -> Unit,
    onItemClick: (DeviceBean) -> Unit
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackBarState = rememberSnackBarState()
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val deviceList by viewModel.deviceListFlow.collectAsStateWithLifecycle()
    val deviceStateMap by viewModel.deviceStateMapFlow.collectAsStateWithLifecycle()
    val cameraPermissionState = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    } else {
        rememberMultiplePermissionsState(
            listOf(
                Manifest.permission.CAMERA,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        )
    }
    val firstCameraPermissionState by remember {
        mutableStateOf(cameraPermissionState.allPermissionsGranted)
    }

    InitializeScope {
        viewModel.shiftCurrentFamily()
    }

    LaunchedEffect(cameraPermissionState.allPermissionsGranted) {
        val isGranted = cameraPermissionState.allPermissionsGranted
        if (firstCameraPermissionState != isGranted && isGranted) {
            TuyaSdkManager.openScan(context)
        }
    }

    if (uiState.loading) {
        LoadingCard()
    }

    LedvanceScreen(
        title = "Home",
        enableTitleActionIcon = true,
        actionIconPainter = painterResource(R.drawable.ic_add),
        onActionPressed = {
            if (cameraPermissionState.allPermissionsGranted) {
                TuyaSdkManager.openScan(context)
            } else {
                cameraPermissionState.launchMultiplePermissionRequest()
            }
        }
    ) {
        PullRefresh(
            isRefreshing = uiState.isRefreshing,
            onRefresh = { viewModel.onRefresh() },
        ) {
            when {
                deviceList.isEmpty() && !uiState.loading -> {
                    val state = rememberScrollState()
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(state),
                        contentAlignment = Alignment.Center
                    ) {
                        LedvanceButton(
                            text = "Add device",
                            modifier = Modifier.padding(horizontal = 100.dp)
                        ) {
                            if (cameraPermissionState.allPermissionsGranted) {
                                TuyaSdkManager.openScan(context)
                            } else {
                                cameraPermissionState.launchMultiplePermissionRequest()
                            }
                        }
                    }
                }

                else -> LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(15.dp),
                    verticalArrangement = Arrangement.spacedBy(15.dp),
                    contentPadding = PaddingValues(20.dp)
                ) {
                    items(
                        items = deviceList,
                        key = { it.devId },
                        span = { GridItemSpan(1) }) { device ->
                        DeviceItem(
                            device = device,
                            isOnline = deviceStateMap[device.devId]?.online ?: false,
                            switch = deviceStateMap[device.devId]?.switch ?: false,
                            onSwitchChange = { device, switch ->
                                scope.launch {
                                    val result = viewModel.switch(device = device, switch = switch)
                                    val errorMsg = result.exceptionOrNull()?.message
                                    if (!errorMsg.isNullOrEmpty()) {
                                        snackBarState.showSnackbar(errorMsg)
                                    }
                                }
                            },
                            onClick = {
                                scope.launch {
                                    onItemClick.invoke(it)
                                }
                            })
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .align(alignment = Alignment.BottomEnd)
                .padding(30.dp),
            horizontalAlignment = Alignment.End
        ) {
            var extended by remember { mutableStateOf(false) }
            AnimatedVisibility(visible = extended) {
                SmallFloatingActionButton(
                    onClick = onGotoTestMode,
                    shape = CircleShape,
                    contentColor = Color.Black,
                    containerColor = Color.White,
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .size(48.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_test_mode),
                        contentDescription = "More"
                    )
                }
            }
            SmallFloatingActionButton(
                onClick = { extended = !extended },
                shape = CircleShape,
                contentColor = Color.Black,
                containerColor = Color.White,
                modifier = Modifier.size(48.dp)
            ) {
                Icon(imageVector = Icons.Filled.MoreVert, contentDescription = "More")
            }
        }
    }
}