package com.ledvance.ai.light.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.ledvance.ai.light.screen.DevicePanelScreen
import com.ledvance.ai.light.screen.HomeScreen
import com.ledvance.ai.light.screen.LoginScreen
import com.ledvance.ai.light.viewmodel.UserViewModel
import timber.log.Timber

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/2 11:26
 * Describe : MainNavigation
 */
@Composable
fun MainNavigation(userViewModel: UserViewModel = hiltViewModel()) {
    val isLogin by userViewModel.isLoginFlow.collectAsStateWithLifecycle()
    Timber.tag("TAG").i("MainNavigation: isLogin:$isLogin")
    val startRoute = if (isLogin) MainRoute else LoginRoute
    val backStack = rememberMutableStateListOf(startRoute)
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryDecorators = listOf(
            rememberSceneSetupNavEntryDecorator(),
            rememberSavedStateNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator(),
        ),
        transitionSpec = {
            // Slide in from right when navigating forward
            slideInHorizontally(initialOffsetX = { it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { -it })
        },
        popTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        predictivePopTransitionSpec = {
            // Slide in from left when navigating back
            slideInHorizontally(initialOffsetX = { -it }) togetherWith
                    slideOutHorizontally(targetOffsetX = { it })
        },
        entryProvider = entryProvider {
            entry<MainRoute> {
                HomeScreen()
            }
            entry<LoginRoute> {
                LoginScreen(onLoginSuccess = {
                    backStack.add(MainRoute)
                    backStack.removeAll { it is LoginRoute }
                })
            }
            entry<DevicePanelRoute> {
                DevicePanelScreen(devId = it.devId, onBackPressed = {
                    backStack.removeLastOrNull()
                })
            }
        })
}