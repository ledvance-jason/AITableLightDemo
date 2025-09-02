package com.ledvance.ai.light.navigation

import kotlinx.serialization.Serializable

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/2 11:28
 * Describe : NavigationRoute
 */
sealed interface NavigationRoute {
    @Serializable
    data object Main : NavigationRoute

    @Serializable
    data object Login : NavigationRoute

    @Serializable
    data object More : NavigationRoute
}
