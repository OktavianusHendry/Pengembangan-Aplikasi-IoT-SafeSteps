package com.okta.iottest.ui.screen.location

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.okta.iottest.R
import com.okta.iottest.model.SavedList

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SavedLocationScreen(
//    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    var inputTempat by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Saved Places",
                        style = MaterialTheme.typography.titleMedium,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
//                        navController.navigate(Screen.About.route) {
//                            popUpTo(Screen.PerkiraanForm.route) { saveState = true }
//                            restoreState = true
//                            launchSingleTop = true
//                        }
                    }) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {  },
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
            ) {
                OutlinedTextField(
                    value = inputTempat,
                    placeholder = { Text("Find your saved places", textAlign = TextAlign.Center) },
                    onValueChange = { newInput ->
                        inputTempat = newInput
                    },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    leadingIcon = {
                        Icon(
                            Icons.Filled.Search,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    },
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 16.dp)
                )
                SavedList.forEach { savedListData ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 16.dp, bottom = 4.dp, end = 16.dp, top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = savedListData.name,
                            style = MaterialTheme.typography.labelLarge,
                            modifier = Modifier
                                .weight(0.9f)
                                .padding(start = 16.dp),
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
                            .padding(horizontal = 16.dp)
                            .height(1.dp)
                            .background(Color.Black)
                    )
//                Row(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(start = 16.dp, bottom = 4.dp, end = 16.dp, top = 16.dp),
//                    verticalAlignment = Alignment.CenterVertically
//                ) {
//                    Icon(
//                        Icons.Filled.LocationOn,
//                        contentDescription =null,
//                        tint = MaterialTheme.colorScheme.primary
//                    )
//                    Text(
//                        text = "Pradita University",
//                        style = MaterialTheme.typography.labelLarge,
//                        modifier = Modifier
//                            .weight(0.9f)
//                            .padding(start = 16.dp),
//                    )
//                    IconButton(onClick = { /*TODO*/ }) {
//                        Icon(
//                            Icons.Filled.KeyboardArrowRight,
//                            contentDescription = "Continue",
//                            modifier = Modifier
//                                .size(24.dp)
//                                .weight(0.1f)
//                        )
//                    }
//                }
//                    Spacer(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .padding(horizontal = 16.dp)
//                            .height(1.dp)
//                            .background(Color.Black)
//                    )
                }
            }
        }
    }
}