package com.ledvance.ai.light.navigation

import kotlinx.serialization.Serializable

/**
 * @author : jason yin
 * Email : j.yin@ledvance.com
 * Created date 2025/9/2 11:28
 * Describe : NavigationRoute
 */
sealed interface NavigationRoute

@Serializable
data object MainRoute : NavigationRoute

@Serializable
data object LoginRoute : NavigationRoute

@Serializable
data class DevicePanelRoute(val devId: String, val devName: String) : NavigationRoute

@Serializable
data object MoreRoute : NavigationRoute

@Serializable
data object TestModeRoute : NavigationRoute

@Serializable
data object AddDevicesRoute : NavigationRoute

@Serializable
data object SelectWiFiNetwork : NavigationRoute

@Serializable
data class ExploreModeRoute(val devName: String) : NavigationRoute
