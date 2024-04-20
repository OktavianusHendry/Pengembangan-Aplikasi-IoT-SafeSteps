package com.okta.iottest.ui.screen.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.okta.iottest.R
import com.okta.iottest.ui.components.ImageSlider

@Preview
@Composable
fun WelcomeScreen(
//    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
){
    val images = listOf(
        Triple(R.drawable.welcome1, "Freedom with Safety", "Give elders the freedom to explore with our fall detection device. Real-time monitoring and emergency alerts keep them safe."),
        Triple(R.drawable.welcome2, "No Worries", "Track whenever your loved ones leave or arrive home. Get notified when they are out of the house for too long."),
        Triple(R.drawable.welcome3, "Safety is number one", "24/7 monitoring and emergency alerts keep your loved ones safe."),
        Triple(R.drawable.welcome4, "Your loved one is safe", "Get notified when your loved one is getting an incident."),
        Triple(R.drawable.welcome5, "Location? We got you", "Track your loved one's location right when they need you."),
    )

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(

            modifier = Modifier
                .padding(horizontal = 8.dp, vertical = 8.dp)
                .fillMaxHeight(0.1f)
        ) {
            Image(
                painter = painterResource(R.drawable.img),
                contentDescription = null,
                modifier = Modifier
                    .align(Center)
                    .fillMaxWidth()
                    .fillMaxHeight(0.5f)
            )
        }
        ImageSlider(images)
        Button(
            onClick = {
//                    navController.navigate(Screen.Login.route){
//                        popUpTo(Screen.Welcome.route) {
//                            saveState = true
//                        }
//                        restoreState = true
//                        launchSingleTop = true
//                    }
            },
            Modifier
                .padding(top = 16.dp, start = 32.dp, end = 32.dp)
                .fillMaxWidth()
                .height(48.dp)
//                .weight(0.5f),
        ) {
            Text(text = "Register now")
        }
        Button(
            onClick = {
//                    navController.navigate(Screen.Signup.route) {
//                        popUpTo(Screen.Welcome.route) { saveState = true }
//                        restoreState = true
//                        launchSingleTop = true
//                    }
            },
            Modifier
                .padding(top = 12.dp, start = 32.dp, end = 32.dp)
                .fillMaxWidth()
                .height(48.dp)
//                .weight(0.5f),
        ) {
            Text(text = "Login to your account")
        }
    }
}