package com.okta.iottest.ui.navigation

import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.graphics.vector.ImageVector

data class NavigationItem(
    val title: String,
    val icon: Painter,
    val screen: Screen
)