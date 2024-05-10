package com.okta.iottest.ui.screen.device

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.okta.iottest.R
import com.okta.iottest.model.PeopleList
import com.okta.iottest.ui.components.BottomBar
import com.okta.iottest.ui.components.DeviceRow
import com.okta.iottest.ui.navigation.Screen
import com.okta.iottest.ui.theme.OnPrimaryContainer
import com.okta.iottest.ui.theme.PrimaryContainer

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun RegisteredDeviceScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Device") },
                navigationIcon = {},
                actions = {
                    // Add action icons here
                          Button(
                              modifier = Modifier
                                  .padding(end = 16.dp),
                              colors = ButtonDefaults.buttonColors(containerColor = PrimaryContainer, contentColor = OnPrimaryContainer),
                              shape = RoundedCornerShape(8.dp),
                              onClick = {
                                  navController.navigate(Screen.AddDevice.route) {
                                      popUpTo(Screen.Device.route) {
                                          saveState = true
                                      }
                                      restoreState = true
                                      launchSingleTop = true
                                  }
                              }
                          ) {
                              Image(
                                  painter = painterResource(id = R.drawable.scan_device),
                                  contentDescription = null,
                                    modifier = Modifier.size(20.dp),
                              )
                              Spacer(modifier = Modifier.padding(6.dp))
                              Text("Scan New Device", style = MaterialTheme.typography.titleMedium)
                          }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.onPrimary,)
            )
        },
        bottomBar = {
            BottomBar(navController)
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            // Add content here
            Spacer(modifier = Modifier.padding(8.dp))
            PeopleList.forEach { data ->
                DeviceRow(
                    profilePicture = data.profilePicture,
                    name = data.name,
                    distance = data.distance,
                    updatedTime = data.updatedTime,
                    status = data.status,
                    location = data.location!!,
                    onClick = { _, _, _, _, _, _ -> }
                )
            }
        }
    }
}
