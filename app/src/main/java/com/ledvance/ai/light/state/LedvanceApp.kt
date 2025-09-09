package com.ledvance.ai.light.state

import androidx.compose.material3.SnackbarHost
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.vector.ImageVector
import com.ledvance.ai.light.navigation.MainNavigation
import com.ledvance.ui.component.LedvanceScreen
import com.ledvance.ui.theme.LocalSnackBarHostState

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
//    val topLevelRoutes = rememberSaveable {
//        mapOf(
//            NavigationRoute.More to NavBarItem(icon = Icons.Default.Home, description = "Home"),
//            NavigationRoute.More to NavBarItem(icon = Icons.Default.Favorite, description = "More")
//        )
//    }
    LedvanceScreen(snackbarHost = { SnackbarHost(LocalSnackBarHostState.current) }) {
        MainNavigation()
    }
}