package com.ledvance.ai.light

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.core.view.WindowCompat
import dagger.hilt.android.AndroidEntryPoint

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/10/9 11:37
 * Describe : ExploreModeActivity
 */
@AndroidEntryPoint
class ExploreModeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black)
            ) {

            }
        }
    }
}