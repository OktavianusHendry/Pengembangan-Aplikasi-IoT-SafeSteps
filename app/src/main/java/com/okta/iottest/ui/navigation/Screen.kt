package com.okta.iottest.ui.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Notification : Screen("notification")
    object Profile : Screen("profile")
    object DetailReward : Screen("home/{rewardId}") {
        fun createRoute(rewardId: Long) = "home/$rewardId"
    }
}