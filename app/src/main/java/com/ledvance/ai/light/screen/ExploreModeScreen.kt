package com.ledvance.ai.light.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.ledvance.ai.light.ui.VideoPlayer
import com.ledvance.ai.light.viewmodel.ExoPlayViewModel
import com.ledvance.ui.component.LedvanceScreen
import com.ledvance.ui.theme.AppTheme

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/10/9 11:12
 * Describe : ExploreModeScreen
 */
@Composable
fun ExploreModeScreen(
    title: String,
    viewModel: ExoPlayViewModel = hiltViewModel(),
    onBackPressed: (() -> Unit)? = null
) {

    LaunchedEffect(Unit) {
        val assetNames = listOf(
            "video/aitablelight.mp4",
        )
        viewModel.loadMediaItem(assetNames)
    }
    LedvanceScreen(
        backTitle = title,
        title = "Explore Mode",
        onBackPressed = onBackPressed,
        modifier = Modifier.padding(horizontal = 24.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                elevation = CardDefaults.cardElevation(8.dp),
                colors = CardDefaults.cardColors(containerColor = AppTheme.colors.screenBackground),
                shape = RoundedCornerShape(10.dp),
            ) {
                VideoPlayer(
                    exoPlayer = viewModel.getPlayer(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1080 / 640f),
                )
            }
        }
    }
}