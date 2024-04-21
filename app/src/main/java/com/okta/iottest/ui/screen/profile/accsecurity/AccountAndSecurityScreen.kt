package com.okta.iottest.ui.screen.profile.accsecurity

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.ExitToApp
import androidx.compose.material.icons.outlined.Lock
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
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
fun AccountAndSecurityScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Account And Security", style= MaterialTheme.typography.titleMedium) },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            contentDescription = "Continue",
                            modifier = Modifier
                                .size(24.dp)
                        )
                    }
                },
                actions = {
                    // Add action icons here
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.onPrimary)
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            Box {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding( start = 16.dp, end = 16.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Top,
                        modifier = modifier
                            .padding(top = 4.dp, bottom = 4.dp)
                            .clickable {  }
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Outlined.Email),
                            contentDescription = null
                        )
                        Column(
                            modifier = Modifier
                                .weight(0.9f)
                                .padding(start = 8.dp),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                text = "Add Recovery Email",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(
                                text = "Incase you want to secure you account more",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.Filled.KeyboardArrowRight,
                                contentDescription = "Continue",
                                modifier = Modifier
                                    .size(24.dp)
                                    .weight(0.1f)
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.Gray)
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Top,
                        modifier = modifier
                            .padding(top = 12.dp, bottom = 12.dp)
                            .clickable {  }
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Outlined.Lock),
                            contentDescription = null
                        )
                        Column(
                            modifier = Modifier
                                .weight(0.9f)
                                .padding(start = 8.dp),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                text = "Change Password",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(
                                text = "Renew you password every 3 months makes your account more secure!",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.Filled.KeyboardArrowRight,
                                contentDescription = "Continue",
                                modifier = Modifier
                                    .size(24.dp)
                                    .weight(0.1f)
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.Gray)
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Top,
                        modifier = modifier
                            .padding(top = 12.dp, bottom = 12.dp)
                            .clickable {  }
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Outlined.ExitToApp),
                            contentDescription = null
                        )
                        Column(
                            modifier = Modifier
                                .weight(0.9f)
                                .padding(start = 8.dp),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                text = "Log Out",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(
                                text = "Are your sure? You can log in again whenever you want, but you will not get any notification once you logged out",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.Filled.KeyboardArrowRight,
                                contentDescription = "Continue",
                                modifier = Modifier
                                    .size(24.dp)
                                    .weight(0.1f)
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.Gray)
                    )
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.Top,
                        modifier = modifier
                            .padding(top = 12.dp, bottom = 12.dp)
                            .clickable {  }
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.trash_icon),
                            contentDescription = null
                        )
                        Column(
                            modifier = Modifier
                                .weight(0.9f)
                                .padding(start = 8.dp),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Text(
                                text = "Remove Account",
                                style = MaterialTheme.typography.labelLarge
                            )
                            Text(
                                text = "Your account will be removed permanently and you can't recover it once you do it",
                                style = MaterialTheme.typography.bodySmall,
                            )
                        }
                        IconButton(onClick = { /*TODO*/ }) {
                            Icon(
                                Icons.Filled.KeyboardArrowRight,
                                contentDescription = "Continue",
                                modifier = Modifier
                                    .size(24.dp)
                                    .weight(0.1f)
                            )
                        }
                    }
                    Spacer(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.Gray)
                    )
                }
            }
        }
    }
}