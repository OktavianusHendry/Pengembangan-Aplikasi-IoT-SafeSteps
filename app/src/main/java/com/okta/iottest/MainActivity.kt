package com.okta.iottest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.okta.iottest.ui.navigation.Screen
import com.okta.iottest.ui.screen.device.DeviceScreen
import com.okta.iottest.ui.screen.location.MapLocationScreen
import com.okta.iottest.ui.screen.notification.NotificationScreen
import com.okta.iottest.ui.screen.profile.ProfileScreen
import com.okta.iottest.ui.screen.profile.accsecurity.AccountAndSecurityScreen
import com.okta.iottest.ui.screen.profile.termscondition.TermsAndConditionScreen
import com.okta.iottest.ui.theme.IoTtestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()
        setContent {
            IoTtestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    val startDestination = Screen.Device.route

                    BackHandler {
                        when (navController.currentDestination?.route) {
                            Screen.Device.route -> finish()
                            Screen.Welcome.route -> finish()
                            else -> navController.popBackStack()
                        }
                    }

                    NavHost(navController = navController, startDestination = startDestination) {
                        composable(Screen.Device.route) {
                            DeviceScreen(navController)
                        }
                        composable(Screen.Notification.route) {
                            NotificationScreen(navController)
                        }
                        composable(Screen.Profile.route) {
                            ProfileScreen(navController)
                        }
                        composable(Screen.Location.route) {
                            MapLocationScreen(navController)
                        }
                        composable(Screen.AccountAndSecurity.route){
                            AccountAndSecurityScreen(navController)
                        }
                        composable(Screen.TermsAndCondition.route){
                            TermsAndConditionScreen(navController)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    IoTtestTheme {
        Greeting("Android")
    }
}