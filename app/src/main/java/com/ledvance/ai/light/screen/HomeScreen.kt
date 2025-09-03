package com.ledvance.ai.light.screen

import android.Manifest
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.ledvance.ai.light.viewmodel.HomeViewModel
import com.ledvance.tuya.TuyaSdkManager
import com.ledvance.ui.R
import com.ledvance.ui.component.InitializeScope
import com.ledvance.ui.component.LedvanceScreen
import com.ledvance.ui.component.LoadingCard
import timber.log.Timber

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/2 17:02
 * Describe : HomeScreen
 */
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    var loading by remember { mutableStateOf(false) }
    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val firstCameraPermissionState by remember {
        mutableStateOf(cameraPermissionState.status.isGranted)
    }
    Timber.tag("TAG").d("HomeScreen: $firstCameraPermissionState ${cameraPermissionState.status}")
    InitializeScope {
        loading = true
        viewModel.shiftCurrentFamily()
        loading = false
    }

    LaunchedEffect(cameraPermissionState.status.isGranted) {
        val isGranted = cameraPermissionState.status.isGranted
        if (firstCameraPermissionState != isGranted && isGranted) {
            TuyaSdkManager.openScan(context)
        }
    }

    if (loading) {
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

    }
}