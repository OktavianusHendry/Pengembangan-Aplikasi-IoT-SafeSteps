package com.okta.iottest.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.okta.iottest.R
import com.okta.iottest.ui.navigation.BottomNavigationItem
import com.okta.iottest.ui.navigation.NavigationItem
import com.okta.iottest.ui.navigation.Screen
import com.okta.iottest.ui.theme.OnPrimary100
import com.okta.iottest.ui.theme.OnPrimaryContainer
import com.okta.iottest.ui.theme.PrimaryContainer

@Composable
fun NavigationBottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavigationBar(
        modifier = modifier
            .height(56.dp)
            .border(0.1.dp, Color.Gray)
            .horizontalScroll(rememberScrollState()),
        containerColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        val navigationItems = listOf(
            BottomNavigationItem(
                title = "Done",
                icon = painterResource(id = R.drawable.baseline_done_24),
            ),
            BottomNavigationItem(
                title = "Directions",
                icon = painterResource(id = R.drawable.baseline_navigation_24),
            ),
            BottomNavigationItem(
                title = "Share",
                icon = painterResource(id = R.drawable.baseline_share_24),
            ),
            BottomNavigationItem(
                title = "Save",
                icon = painterResource(id = R.drawable.baseline_bookmark_24),
            ),
        )

        navigationItems.mapIndexed { index, item ->
            CustomNavigationBarItem(
                icon = item.icon,
                label = item.title,
                isFirstItem = index == 0,
                onClick = {
//                    navController.navigate(item.screen.route) {
//                        popUpTo(navController.graph.findStartDestination().id) {
//                            saveState = true
//                        }
//                        restoreState = true
//                        launchSingleTop = true
//                    }
                },
                isLastItem = index == navigationItems.lastIndex // Check if this is the last item
            )
        }
    }
}

@Composable
fun CustomNavigationBarItem(
    icon: Painter,
    label: String,
    isFirstItem: Boolean,
    onClick: () -> Unit,
    isLastItem: Boolean
) {
    Box(
        modifier = Modifier
            .height(64.dp)
            .padding(
                top = 10.dp,
                start = 16.dp,
                bottom = 10.dp,
                end = if (isLastItem) 16.dp else 0.dp
            )
            .clip(RoundedCornerShape(4.dp))
            .background(if (isFirstItem) MaterialTheme.colorScheme.primary else PrimaryContainer)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp),
        ) {
            Icon(
                painter = icon,
                contentDescription = label,
                modifier = Modifier
                    .size(18.dp),
                tint = if (isFirstItem) OnPrimary100 else OnPrimaryContainer // Change icon color based on selection
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                label,
                textAlign = TextAlign.End,
                color = if (isFirstItem) OnPrimary100 else OnPrimaryContainer,
                style = MaterialTheme.typography.titleSmall,
            )
        }
    }
}