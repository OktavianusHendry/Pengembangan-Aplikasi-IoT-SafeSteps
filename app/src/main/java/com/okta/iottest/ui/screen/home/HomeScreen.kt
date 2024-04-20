package com.okta.iottest.ui.screen.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
fun HomeScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
){
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("Devices") },
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
                painter = painterResource(R.drawable.device),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 64.dp)
                    .height(200.dp),
            )
            Text(
                text = "No registered devices",
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp
                ),
                modifier = Modifier
                    .padding(16.dp)
                    .align(CenterHorizontally),
                textAlign = TextAlign.Center,
            )
            Text(
                text = "Add your device to monitor your loved ones with ease",
                style = TextStyle(
                    fontWeight = FontWeight.Normal,
                    fontSize = 14.sp
                ),
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .align(CenterHorizontally),
                textAlign = TextAlign.Center,

            )
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier
                    .padding(16.dp)
                    .height(44.dp)
                    .fillMaxWidth()
                    .align(CenterHorizontally),

            ) {
                Icon(Icons.Default.Add, contentDescription = "Add") // Add this line
                Spacer(Modifier.width(8.dp))
                Text(text = "Add Device")
            }
            Spacer(modifier = Modifier.padding(bottom = 24.dp))
        }
    }
}