package com.okta.iottest.ui.navigation

sealed class Screen(val route: String) {
    object Device : Screen("device")
    object Welcome : Screen("welcome")
    object Notification : Screen("notification")
    object Profile : Screen("profile")
    object Location : Screen("location")
    object AccountAndSecurity : Screen("accountandsecurity")
    object TermsAndCondition : Screen("termsandcondition")
    object Signup : Screen("signup")
    object Login : Screen("login")
    object EditProfile : Screen("editprofile")
    object DetailReward : Screen("home/{rewardId}") {
        fun createRoute(rewardId: Long) = "home/$rewardId"
    }
}