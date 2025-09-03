package com.ledvance.ai.light.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import com.ledvance.ai.light.viewmodel.DevicePanelViewModel
import com.ledvance.ui.component.LedvanceScreen
import dagger.hilt.android.lifecycle.HiltViewModel

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/3 14:48
 * Describe : DevicePanelScreen
 */
@Composable
fun DevicePanelScreen(
    devId: String,
    onBackPressed: () -> Unit,
    viewModel: DevicePanelViewModel = hiltViewModel()
) {
    LedvanceScreen(backTitle = "Home", onBackPressed = onBackPressed) {

    }
}