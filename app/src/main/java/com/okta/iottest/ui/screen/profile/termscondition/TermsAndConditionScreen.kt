package com.okta.iottest.ui.screen.profile.termscondition

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun TermsAndConditionScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Terms and Condition",
                        style = MaterialTheme.typography.titleMedium
                    )
                },
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
            ) {
                Text(text = "Last Updated April 13, 2024", style = MaterialTheme.typography.titleSmall)
                Text(text = "We are Safe Steps (\"Company,\" \"we,\" \"us,\" \"our\"), a company registered in Indonesia at Scientia Business Park, Jl. Gading Serpong Boulevard No.1 Tower 1, Curug Sangereng, Kec. Klp. Dua, Kabupaten Tangerang, Banten 15810, Tangerang, Banten 15810.\n" +
                        "\n" +
                        "We operate the mobile application Safe Steps (the \"App\"), as well as any other related products and services that refer or link to these legal terms (the \"Legal Terms\") (collectively, the \"Services\").\n" +
                        "\n" +
                        "SafeSteps is a device that can be used by elderly people to track their movements and location. This device utilizes gyroscope and GPS technology to detect the user's orientation and position. This device can send a warning to family or caregivers if the elderly person gets lost. SafeSteps is designed to provide peace and security for seniors and their families.\n" +
                        "\n" +
                        "You can contact us by email at safesteps@gmail.com or by mail to Scientia Business Park, Jl. Gading Serpong Boulevard No.1 Tower 1, Curug Sangereng, Kec. Klp. Dua, Kabupaten Tangerang, Banten 15810, Tangerang, Banten 15810, Indonesia.\n" +
                        "\n" +
                        "These Legal Terms constitute a legally binding agreement made between you, whether personally or on behalf of an entity (\"you\"), and Safe Steps, concerning your access to and use of the Services. You agree that by accessing the Services, you have read, understood, and agreed to be bound by all of these Legal Terms. IF YOU DO NOT AGREE WITH ALL OF THESE LEGAL TERMS, THEN YOU ARE EXPRESSLY PROHIBITED FROM USING THE SERVICES AND YOU MUST DISCONTINUE USE IMMEDIATELY.\n" +
                        "\n" +
                        "We will provide you with prior notice of any scheduled changes to the Services you are using. The modified Legal Terms will become effective upon posting or notifying you by safesteps@gmail.com, as stated in the email message. By continuing to use the Services after the effective date of any changes, you agree to be bound by the modified terms.\n" +
                        "\n" +
                        "The Services are intended for users who are at least 13 years of age. All users who are minors in the jurisdiction in which they reside (generally under the age of 18) must have the permission of, and be directly supervised by, their parent or guardian to use the Services. If you are a minor, you must have your parent or guardian read and agree to these Legal Terms prior to you using the Services.\n",
                    style = MaterialTheme.typography.bodySmall)
            }

        }
    }
}