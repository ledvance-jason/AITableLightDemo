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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ledvance.ai.light.model.CustomActions
import com.ledvance.ai.light.model.Mode
import com.ledvance.ai.light.model.Scenes
import com.ledvance.ai.light.model.WorkModeSegment
import com.ledvance.ai.light.ui.FlowRowSection
import com.ledvance.ai.light.ui.ModeView
import com.ledvance.ai.light.ui.ScenesView
import com.ledvance.ai.light.viewmodel.DevicePanelViewModel
import com.ledvance.ui.component.IRadioGroupItem
import com.ledvance.ui.component.LedvanceButton
import com.ledvance.ui.component.LedvanceRadioGroup
import com.ledvance.ui.component.LedvanceScreen
import com.ledvance.ui.component.LedvanceSlider
import com.ledvance.ui.component.LedvanceSwitch
import com.ledvance.ui.component.LoadingCard
import com.ledvance.ui.component.rememberSnackBarState
import com.ledvance.ui.component.workmode.ColorModePicker
import com.ledvance.ui.component.workmode.WhiteModePicker
import com.ledvance.ui.extensions.clipWithBorder
import com.ledvance.ui.theme.AppTheme
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
    val scope = rememberCoroutineScope()
    val snackBarState = rememberSnackBarState()
    val state = rememberScrollState()
    val deviceStateMap by viewModel.deviceStateMapFlow.collectAsStateWithLifecycle()
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
                .padding(horizontal = 24.dp)
        ) {

            LightingControl(
                switch = deviceStateMap[devId]?.switch ?: false,
            ) {
                scope.launch {
                    val result = viewModel.switch(it)
                    val errorMsg = result.exceptionOrNull()?.message
                    if (!errorMsg.isNullOrEmpty()) {
                        snackBarState.showSnackbar(errorMsg)
                    }
                }
            }

            ModeView(
                items = Mode.allMode,
                title = "Mode",
                modifier = Modifier.padding(top = 20.dp),
                onItemClick = {

                }
            )

            ScenesView(
                modifier = Modifier.padding(top = 20.dp),
                items = Scenes.allScenes,
                title = "Scenes"
            ) {

            }
            ScenesView(
                modifier = Modifier.padding(top = 20.dp),
                items = Scenes.lightEffect,
                maxItemsInEachRow = 2,
                title = "Light Effect"
            ) {

            }

            FlowRowSection(
                items = CustomActions.allActions,
                title = "Custom Actions",
                modifier = Modifier.padding(top = 20.dp),
                onItemClick = {

                }
            )

            Text(
                text = "Volume",
                color = AppTheme.colors.title,
                style = AppTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 20.dp, bottom = 15.dp)
            )
            LedvanceSlider(
                value = 100,
                onValueChange = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp)
                    .clipWithBorder(
                        shape = CircleShape,
                        borderColor = AppTheme.colors.border,
                        borderWidth = 2.dp
                    )
            )

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
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
private fun LightingControl(
    switch: Boolean,
    modifier: Modifier = Modifier,
    onSwitchChange: (Boolean) -> Unit
) {
    val allWorkMode = remember { WorkModeSegment.allWorkModeSegment }
    var selectedWorkMode by remember {
        mutableStateOf<IRadioGroupItem>(allWorkMode.first())
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
                checked = switch,
                onCheckedChange = {
                    onSwitchChange.invoke(it)
                }
            )
        }

        if (switch) {
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
                    selectedWorkMode = it
                }
            )

            if (selectedWorkMode.id == WorkModeSegment.ColorMode.id) {
                ColorModePicker(
                    hue = 0, sat = 0, brightness = 50,
                    onHsv = { h, s, v -> },
                    onHsvComplete = { h, s, v -> })
            } else {
                WhiteModePicker(
                    cct = 0, brightness = 50,
                    onBrightnessChanged = {},
                    onCCTChanged = { cct, color -> },
                )
            }
        }
    }
}

