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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.okta.iottest.R

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
) {
    var showDialog by remember { mutableStateOf(false) }
    var dialogText by remember { mutableStateOf("") }

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
                Image(
                    painter = painterResource(R.drawable.welcome5),
                    contentDescription = null,
                    modifier = modifier
                        .size(75.dp)
                        .clip(shape = CircleShape)
                        .aspectRatio(1f)
                )

                Spacer(modifier = Modifier.width(16.dp)) // Add this line

                Column {
                    Text(
                        text = "John Doe",
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        modifier = modifier.padding(bottom = 4.dp)
                    )
                    Text(
                        text = "johndoe@gmail.com",
                        style = TextStyle(
                            fontWeight = FontWeight.Normal,
                            fontSize = 14.sp
                        )
                    )
                }

                Spacer(Modifier.weight(1f, true)) // Add this line

                IconButton(onClick = { /*TODO*/ }) {
                    Icon(
                        painter = painterResource(R.drawable.baseline_mode_edit_outline_24),
                        contentDescription = null,
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Account & Security",
                modifier = modifier
                    .padding(bottom = 16.dp)
                    .clickable { /*TODO*/ },
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
            )
            Text(
                text = "Dark Mode",
                modifier = modifier
                    .padding(bottom = 16.dp)
                    .clickable { /*TODO*/ },
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
            )
            val context = LocalContext.current
            Text(
                text = "Language",
                modifier = modifier
                    .padding(bottom = 16.dp)
                    .clickable {
                        val intent = Intent(Settings.ACTION_LOCALE_SETTINGS)
                        context.startActivity(intent)
                    },
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
            )
            Text(
                text = "Help Center",
                modifier = modifier
                    .padding(bottom = 16.dp)
                    .clickable { /*TODO*/ },
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
            )
            Text(
                text = "About",
                modifier = modifier
                    .padding(bottom = 16.dp)
                    .clickable {
                        dialogText =
                            "ECOVACS’ more than 24 years of work in service robotics has seen the company transformed from a visionary startup into a global corporation with a Mission of Robotics for All. Designed to change the way people live and work, ECOVACS robotics are now being used in over 145 countries and regions around the world."
                        showDialog = true
                    },
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
            )
            Text(
                text = "Terms and Condition",
                modifier = modifier
                    .padding(bottom = 16.dp)
                    .clickable {
                        dialogText =
                            "These terms and conditions outline the rules and regulations for the use of SafeSteps's application, located at safesteps.com.\n" +
                                    "\n" +
                                    "By accessing this application we assume you accept these terms and conditions. Do not continue to use SafeSteps if you do not agree to take all of the terms and conditions stated on this page.\n" +
                                    "\n" +
                                    "The following terminology applies to these Terms and Conditions, Privacy Statement and Disclaimer Notice and all Agreements: \"Client\", \"You\" and \"Your\" refers to you, the person log on this application and compliant to the Company's terms and conditions. \"The Company\", \"Ourselves\", \"We\", \"Our\" and \"Us\", refers to our Company. \"Party\", \"Parties\", or \"Us\", refers to both the Client and ourselves. All terms refer to the offer, acceptance and consideration of payment necessary to undertake the process of our assistance to the Client in the most appropriate manner for the express purpose of meeting the Client's needs in respect of provision of the Company's stated services, in accordance with and subject to, prevailing law of id. Any use of the above terminology or other words in the singular, plural, capitalization and/or he/she or they, are taken as interchangeable and therefore as referring to same."
                        showDialog = true
                    },
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
            )
            Text(
                text = "Rate Us",
                modifier = modifier
                    .padding(bottom = 16.dp)
                    .clickable { /*TODO*/ },
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
            )
        }
        if (showDialog) {
            // Semi-transparent background that covers the screen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6F))
            )
            Dialog(onDismissRequest = { showDialog = false }) {
                Box(
                    modifier = Modifier
                        .background(
                            androidx.compose.material3.MaterialTheme.colorScheme.surface,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(text = dialogText)
                }
            }
        }
    }
}
