package com.okta.iottest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.okta.iottest.ui.screen.home.HomeScreen
import com.okta.iottest.ui.screen.location.LocationScreen
import com.okta.iottest.ui.screen.location.MapLocationScreen
import com.okta.iottest.ui.screen.location.SavedLocationScreen
import com.okta.iottest.ui.screen.login.LoginScreen
import com.okta.iottest.ui.screen.notification.NotificationScreen
import com.okta.iottest.ui.screen.profile.ProfileScreen
import com.okta.iottest.ui.screen.signup.SignupScreen
import com.okta.iottest.ui.screen.welcome.WelcomeScreen
import com.okta.iottest.ui.theme.IoTtestTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IoTtestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MapLocationScreen()
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