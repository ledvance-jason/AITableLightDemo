package com.ledvance.ai.light.screen

import android.Manifest
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.ledvance.ai.light.ui.DeviceItem
import com.ledvance.ai.light.viewmodel.HomeViewModel
import com.ledvance.tuya.TuyaSdkManager
import com.ledvance.ui.R
import com.ledvance.ui.component.InitializeScope
import com.ledvance.ui.component.LedvanceButton
import com.ledvance.ui.component.LedvanceScreen
import com.ledvance.ui.component.LoadingCard
import com.ledvance.ui.component.PullRefresh
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
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel(), onItemClick: (DeviceBean) -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val deviceList by viewModel.deviceListFlow.collectAsStateWithLifecycle()
    val deviceStateMap by viewModel.deviceStateMapFlow.collectAsStateWithLifecycle()
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val firstCameraPermissionState by remember {
        mutableStateOf(cameraPermissionState.status.isGranted)
    }

    InitializeScope {
        viewModel.shiftCurrentFamily()
    }

    LaunchedEffect(cameraPermissionState.status.isGranted) {
        val isGranted = cameraPermissionState.status.isGranted
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
            if (cameraPermissionState.status.isGranted) {
                TuyaSdkManager.openScan(context)
            } else {
                cameraPermissionState.launchPermissionRequest()
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
                            if (cameraPermissionState.status.isGranted) {
                                TuyaSdkManager.openScan(context)
                            } else {
                                cameraPermissionState.launchPermissionRequest()
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
                                viewModel.switch(devId = device.devId, switch = switch)
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
    }
}