package com.okta.iottest.ui.screen.notification

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.okta.iottest.R
import com.okta.iottest.ui.components.BottomBar
import com.okta.iottest.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NotificationScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Notification") },
                navigationIcon = {},
                actions = {
                    // Add action icons here
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.onPrimary,)
            )
        },
        bottomBar = {
            if (currentRoute != Screen.DetailReward.route) { //Change
                BottomBar(navController)
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Image(
                painter = painterResource(R.drawable.notification1),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp)
                    .height(200.dp),
            )
            Text(
                text = "No notification?",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,
            )
            Text(
                text = "No worries! Every notification from registered devices would be stored in here!",
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally),
                textAlign = TextAlign.Center,

                )
            Spacer(modifier = Modifier.padding(bottom = 24.dp))
        }
    }
}