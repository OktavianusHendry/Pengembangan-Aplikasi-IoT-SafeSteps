package com.okta.iottest.ui.components

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.NavHostController
import com.okta.iottest.R
import com.okta.iottest.ui.navigation.NavigationItem
import com.okta.iottest.ui.navigation.Screen

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier.height(56.dp).border(0.1.dp, Color.Gray),
        containerColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                title = "Location",
                icon = rememberVectorPainter(image = Icons.Outlined.LocationOn),
                screen = Screen.Location
            ),
            NavigationItem(
                title = "Device",
                icon = painterResource(id = R.drawable.device_icon1),
                screen = Screen.Device
            ),
            NavigationItem(
                title = "Notification",
                icon = rememberVectorPainter(image = Icons.Outlined.Notifications),
                screen = Screen.Notification
            ),
            NavigationItem(
                title = "Profile",
                icon = rememberVectorPainter(image = Icons.Outlined.AccountCircle),
                screen = Screen.Profile
            ),
        )
        Log.d("currentRoute", currentRoute.toString())
        navigationItems.map { item ->
            NavigationBarItem(
                icon = {
                    Icon(
                        painter = item.icon,
                        contentDescription = item.title
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary, // Color when selected
//                    unselectedIconColor = Color.Gray, // Color when not selected
                ),
//                label = { Text(item.title) },
//                selected = true,
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        restoreState = true
                        launchSingleTop = true
                    }
                },
            )
        }
    }
}