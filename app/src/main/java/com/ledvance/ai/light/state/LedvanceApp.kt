package com.ledvance.ai.light.state

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.graphics.vector.ImageVector
import com.ledvance.ai.light.navigation.NavigationRoute

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/2 13:34
 * Describe : LedvanceApp
 */

data class NavBarItem(
    val icon: ImageVector,
    val description: String
)

@Composable
fun LedvanceApp() {
    val topLevelRoutes = rememberSaveable {
        mapOf(
            NavigationRoute.More to NavBarItem(icon = Icons.Default.Home, description = "Home"),
            NavigationRoute.More to NavBarItem(icon = Icons.Default.Favorite, description = "More")
        )
    }
}