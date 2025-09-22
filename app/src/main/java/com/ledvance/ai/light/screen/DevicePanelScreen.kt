package com.ledvance.ai.light.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledvance.ai.light.model.CustomAction
import com.ledvance.ai.light.model.Mode
import com.ledvance.ai.light.model.Scene
import com.ledvance.ai.light.model.WorkModeSegment
import com.ledvance.ai.light.ui.FlowRowSection
import com.ledvance.ai.light.ui.ModeView
import com.ledvance.ai.light.ui.ScenesView
import com.ledvance.ai.light.utils.DataStoreKeys
import com.ledvance.ai.light.viewmodel.DevicePanelViewModel
import com.ledvance.ui.component.LedvanceButton
import com.ledvance.ui.component.LedvanceRadioGroup
import com.ledvance.ui.component.LedvanceScreen
import com.ledvance.ui.component.LedvanceSlider
import com.ledvance.ui.component.LedvanceSwitch
import com.ledvance.ui.component.LoadingCard
import com.ledvance.ui.component.checkShowToast
import com.ledvance.ui.component.rememberSnackBarState
import com.ledvance.ui.component.workmode.ColorModePicker
import com.ledvance.ui.component.workmode.WhiteModePicker
import com.ledvance.ui.extensions.clipWithBorder
import com.ledvance.ui.theme.AppTheme
import com.ledvance.utils.extensions.getBoolean
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/3 14:48
 * Describe : DevicePanelScreen
 */
@Composable
fun DevicePanelScreen(
    devId: String,
    devName: String,
    onBackPressed: () -> Unit,
    viewModel: DevicePanelViewModel = hiltViewModel<DevicePanelViewModel, DevicePanelViewModel.Factory> {
        it.create(devId)
    }
) {
    val uiState by viewModel.uiStateFlow.collectAsStateWithLifecycle()
    val enableDeviceDeleteButton by DataStoreKeys.enableDeviceDeleteButton.getBoolean()
        .map { it ?: false }
        .collectAsStateWithLifecycle(false)
    val scope = rememberCoroutineScope()
    val snackBarState = rememberSnackBarState()
    val state = rememberScrollState()
    if (uiState.loading) {
        LoadingCard()
    }
    LedvanceScreen(
        backTitle = "Home",
        title = devName,
        onBackPressed = onBackPressed,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(state)
                .padding(horizontal = 20.dp)
        ) {

            LightingControl(viewModel = viewModel, snackBarState = snackBarState)
            ArmControl(viewModel = viewModel, snackBarState = snackBarState)

            if (enableDeviceDeleteButton) {
                LedvanceButton(
                    text = "Delete",
                    modifier = Modifier.padding(top = 20.dp),
                    containerColor = AppTheme.colors.buttonGreyBackground
                ) {
                    scope.launch {
                        if (viewModel.delete()) {
                            onBackPressed.invoke()
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
private fun ArmControl(
    viewModel: DevicePanelViewModel,
    snackBarState: SnackbarHostState,
) {
    val scope = rememberCoroutineScope()
    val armUIState by viewModel.armUIStateFlow.collectAsStateWithLifecycle()
    val selectedScene by rememberUpdatedState(Scene.allScenes.find {
        val (scene, enable) = armUIState.sceneData ?: return@find false
        it.id == scene.value && enable
    })
    val selectedLightEffect by rememberUpdatedState(Scene.lightEffect.find {
        val (lightEffect, enable) = armUIState.lightEffectData ?: return@find false
        it.id == lightEffect.value && enable
    })
    var volume by remember { mutableIntStateOf(armUIState.volume) }
    LaunchedEffect(armUIState.volume) {
        volume = armUIState.volume
    }
    ModeView(
        items = Mode.allMode,
        title = "Mode",
        modifier = Modifier.padding(top = 20.dp),
        onItemClick = {
            scope.launch {
                val result = viewModel.setMode(it.id)
                snackBarState.checkShowToast(result)
            }
        }
    )

    ScenesView(
        modifier = Modifier.padding(top = 20.dp),
        selectedItem = selectedScene,
        items = Scene.allScenes,
        title = "Scenes"
    ) {
        scope.launch {
            val result = viewModel.setScene(it.id)
            snackBarState.checkShowToast(result)
        }
    }

    ScenesView(
        modifier = Modifier.padding(top = 20.dp),
        items = Scene.lightEffect,
        selectedItem = selectedLightEffect,
        maxItemsInEachRow = 2,
        title = "Light Effect"
    ) {
        scope.launch {
            val result = viewModel.setLightEffect(it.id)
            snackBarState.checkShowToast(result)
        }
    }

    FlowRowSection(
        items = CustomAction.allActions,
        title = "Custom Actions",
        modifier = Modifier.padding(top = 20.dp),
        onItemClick = {
            scope.launch {
                val result = viewModel.setCustomAction(it.id)
                snackBarState.checkShowToast(result)
            }
        }
    )

    Text(
        text = "Volume",
        color = AppTheme.colors.title,
        style = AppTheme.typography.titleMedium,
        modifier = Modifier.padding(top = 20.dp, bottom = 15.dp)
    )
    LedvanceSlider(
        value = volume,
        onValueChange = {
            volume = it
        },
        onValueComplete = {
            viewModel.setVolume(it)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(45.dp)
            .clipWithBorder(
                shape = CircleShape,
                borderColor = AppTheme.colors.border,
                borderWidth = 2.dp
            )
    )
}

@Composable
private fun LightingControl(
    viewModel: DevicePanelViewModel,
    snackBarState: SnackbarHostState,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val deviceState by viewModel.lightStateFlow.collectAsStateWithLifecycle()
    val allWorkMode = remember { WorkModeSegment.allWorkModeSegment }
    var selectedWorkMode by remember(deviceState.workMode) {
        mutableStateOf(WorkModeSegment.ofWorkMode(deviceState.workMode))
    }
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .then(modifier)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Lighting",
                style = AppTheme.typography.titleMedium,
                color = AppTheme.colors.title,
                modifier = Modifier.weight(1f)
            )
            LedvanceSwitch(
                checked = deviceState.switch,
                onCheckedChange = {
                    scope.launch {
                        val result = viewModel.switch(it)
                        snackBarState.checkShowToast(result)
                    }
                }
            )
        }

        if (deviceState.switch) {
            LedvanceRadioGroup(
                selectorItem = selectedWorkMode,
                items = allWorkMode,
                modifier = Modifier.padding(top = 20.dp, bottom = 15.dp),
                shape = RoundedCornerShape(8.dp),
                checkedColor = Color.White,
                backgroundColor = AppTheme.colors.border,
                checkedTextColor = AppTheme.colors.title,
                textColor = AppTheme.colors.title,
                onCheckedChange = {
                    scope.launch {
                        val result = viewModel.setWorkMode(it.value)
                        snackBarState.checkShowToast(result)
                    }
                }
            )

            if (selectedWorkMode == WorkModeSegment.ColorMode) {
                ColorModePicker(
                    hue = deviceState.hsv.h,
                    sat = deviceState.hsv.s,
                    brightness = deviceState.hsv.v,
                    onHsv = { h, s, v ->
                        viewModel.controlHsv(h, s, v)
                    },
                    onHsvComplete = { h, s, v ->
                        viewModel.setHsv(h, s, v)
                    })
            } else {
                WhiteModePicker(
                    cct = deviceState.cctBrightness.cct,
                    brightness = deviceState.cctBrightness.brightness,
                    onBrightnessChanged = {
                        viewModel.controlCctBrightness(deviceState.cctBrightness.cct, it)
                    },
                    onBrightnessComplete = {
                        viewModel.setCctBrightness(deviceState.cctBrightness.cct, it)
                    },
                    onCCTChanged = { cct, color ->
                        viewModel.controlCctBrightness(cct, deviceState.cctBrightness.brightness)
                    },
                    onCCTComplete = { cct, color ->
                        viewModel.setCctBrightness(cct, deviceState.cctBrightness.brightness)
                    }
                )
            }
        }
    }
}

