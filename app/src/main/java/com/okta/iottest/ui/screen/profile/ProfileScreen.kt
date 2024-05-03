package com.okta.iottest.ui.screen.profile

import android.content.Intent
import android.provider.Settings
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Build
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.okta.iottest.R
import com.okta.iottest.ui.components.BottomBar
import com.okta.iottest.ui.navigation.Screen

@Composable
fun ProfileScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val viewModel: ProfileViewModel = viewModel()
    val email by viewModel.email.collectAsState()
    val name by viewModel.name.collectAsState()
    val photoUrl by viewModel.photoUrl.collectAsState()

    Scaffold(
        bottomBar = {
            BottomBar(navController)
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
                        .padding(top = 32.dp, start = 16.dp, end = 16.dp),
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = CenterVertically
                    ) {
                        Row(Modifier.weight(0.9f)) {
                            Image(
                                painter = rememberImagePainter(data = photoUrl, builder = {
                                    placeholder(R.drawable.welcome5)
                                }),
                                contentScale = ContentScale.Crop,
                                contentDescription = null,
                                modifier = modifier
                                    .size(75.dp)
                                    .clip(shape = CircleShape)
                                    .aspectRatio(1f)
                            )

                            Spacer(modifier = Modifier.width(16.dp)) // Add this line

                            Column {
                                Text(
                                    text = name,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 20.sp
                                    ),
                                    modifier = modifier.padding(bottom = 4.dp)
                                )
                                Text(
                                    text = email,
                                    style = TextStyle(
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 14.sp
                                    )
                                )
                            }
                            Spacer(Modifier.weight(1f, true)) // Add this line
                        }
                        Row (Modifier.weight(0.1f)){
                            IconButton(
                                onClick = {
                                    navController.navigate(Screen.EditProfile.route) {
                                        popUpTo(Screen.Profile.route) {
                                            saveState = true
                                        }
                                        restoreState = true
                                        launchSingleTop = true
                                    }
                                },
                                modifier = modifier
                                    .size(24.dp)
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.outline_mode_edit_outline_24),
                                    contentDescription = null,
                                )
                            }
                        }




                    }

                    Spacer(modifier = modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = CenterVertically,
                        modifier = modifier
                            .padding(vertical = 4.dp)
                            .clickable {
                                navController.navigate(Screen.AccountAndSecurity.route) {
                                    popUpTo(Screen.Profile.route) {
                                        saveState = true
                                    }
                                    restoreState = true
                                    launchSingleTop = true
                                }
                            },
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Outlined.AccountCircle),
                            contentDescription = null
                        )
                        Text(
                            text = "Account & Security",
                            textAlign = TextAlign.Start,
                            modifier = modifier
                                .padding(start = 8.dp)
                                .weight(0.9f),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                        )
                        IconButton(onClick = {
                            navController.navigate(Screen.AccountAndSecurity.route) {
                                popUpTo(Screen.Profile.route) {
                                    saveState = true
                                }
                                restoreState = true
                                launchSingleTop = true
                            }
                        }) {
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
                        verticalAlignment = CenterVertically,
                        modifier = modifier
                            .padding(vertical = 4.dp)
                            .clickable { /*TODO*/ },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_mode_night_24),
                            contentDescription = null
                        )
                        Text(
                            text = "Dark Mode",
                            textAlign = TextAlign.Start,
                            modifier = modifier
                                .padding(start = 8.dp)
                                .weight(0.9f),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                        )
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
                    val context = LocalContext.current
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = CenterVertically,
                        modifier = modifier
                            .padding(vertical = 4.dp)
                            .clickable {
                                val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                                context.startActivity(intent)
                            },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.global),
                            contentDescription = null
                        )
                        Text(
                            text = "Language",
                            modifier = modifier
                                .weight(0.9f)
                                .padding(start = 8.dp),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                        )
                        IconButton(onClick = {
                            val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                            context.startActivity(intent)
                        }) {
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
                        verticalAlignment = CenterVertically,
                        modifier = modifier
                            .padding(vertical = 4.dp)
                            .clickable { /*TODO*/ },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.outline_live_help_24),
                            contentDescription = null
                        )
                        Text(
                            text = "Help Center",
                            modifier = modifier
                                .padding(start = 8.dp)
                                .weight(0.9f),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                        )
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
                        verticalAlignment = CenterVertically,
                        modifier = modifier
                            .padding(vertical = 4.dp)
                            .clickable {
                                navController.navigate(Screen.TermsAndCondition.route) {
                                    popUpTo(Screen.Profile.route) {
                                        saveState = true
                                    }
                                    restoreState = true
                                    launchSingleTop = true
                                }
                            },
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.tnc_icon),
                            contentDescription = null
                        )
                        Text(
                            text = "Terms and Condition",
                            modifier = modifier
                                .padding(start = 8.dp)
                                .weight(0.9f),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                        )
                        IconButton(onClick = {
                            navController.navigate(Screen.TermsAndCondition.route) {
                                popUpTo(Screen.Profile.route) {
                                    saveState = true
                                }
                                restoreState = true
                                launchSingleTop = true
                            }
                        }) {
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
                        verticalAlignment = CenterVertically,
                        modifier = modifier
                            .padding(vertical = 4.dp)
                            .clickable {},
                    ) {
                        Icon(
                            painter = rememberVectorPainter(image = Icons.Outlined.Star),
                            contentDescription = null
                        )
                        Text(
                            text = "Rate Us",
                            modifier = modifier
                                .padding(start = 8.dp)
                                .weight(0.9f),
                            style = TextStyle(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                        )
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

//dialogText =
//"These terms and conditions outline the rules and regulations for the use of SafeSteps's application, located at safesteps.com.\n" +
//"\n" +
//"By accessing this application we assume you accept these terms and conditions. Do not continue to use SafeSteps if you do not agree to take all of the terms and conditions stated on this page.\n" +
//"\n" +
//"The following terminology applies to these Terms and Conditions, Privacy Statement and Disclaimer Notice and all Agreements: \"Client\", \"You\" and \"Your\" refers to you, the person log on this application and compliant to the Company's terms and conditions. \"The Company\", \"Ourselves\", \"We\", \"Our\" and \"Us\", refers to our Company. \"Party\", \"Parties\", or \"Us\", refers to both the Client and ourselves. All terms refer to the offer, acceptance and consideration of payment necessary to undertake the process of our assistance to the Client in the most appropriate manner for the express purpose of meeting the Client's needs in respect of provision of the Company's stated services, in accordance with and subject to, prevailing law of id. Any use of the above terminology or other words in the singular, plural, capitalization and/or he/she or they, are taken as interchangeable and therefore as referring to same."
//showDialog = true