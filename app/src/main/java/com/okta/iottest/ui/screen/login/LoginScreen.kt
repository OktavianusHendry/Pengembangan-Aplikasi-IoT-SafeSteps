package com.okta.iottest.ui.screen.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode.Companion.Screen
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.firebase.auth.FirebaseAuth
import com.okta.iottest.R
import com.okta.iottest.ui.components.EmailText
import com.okta.iottest.ui.components.PasswordText

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun LoginScreen(
//    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    var isLoading by remember { mutableStateOf(false) }

    var inputEmail by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var inputPassword by remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }
    val loginResponse = remember { mutableStateOf(false) }
//    val auth = FirebaseAuth.getInstance()

    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier.fillMaxWidth()) {
            Box(

                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 8.dp)
                    .fillMaxHeight(0.1f)
            ) {
                IconButton(
                    onClick = { /* Handle back arrow click */ },
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.back_arrow),
                        contentDescription = "Back",
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.8f)
                    )
                }
                Image(
                    painter = painterResource(R.drawable.img),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth()
                        .fillMaxHeight(0.5f)
                )
            }
            Text(
                text = "Login",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp)
            )
            Text(
                text = "Login with your credentials",
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Text(
                text = "Email",
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 8.dp)
            )
            EmailText(
                email = inputEmail,
                onEmailChange = { newEmail -> inputEmail = newEmail },
                isEmailValid = isEmailValid,
                onEmailValidChange = { isValid -> isEmailValid = isValid }
            )
            Text(
                text = "Password",
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
            )
            PasswordText(
                input = inputPassword,
                onValueChange = { inputPassword = it },
                isError = false,
                isTapped = false
            )
            Button(
                onClick = {
                    isLoading = true
//                    auth.signInWithEmailAndPassword(inputEmail, inputPassword)
//                        .addOnCompleteListener { task ->
//                            isLoading = false
//                            if (task.isSuccessful) {
//                                // User is successfully signed in
//                                openDialog.value = true
//                                loginResponse.value = true
//                            } else {
//                                // Sign in failed
//                                openDialog.value = true
//                                loginResponse.value = false
//                            }
//                        }

                },
                enabled = isEmailValid && inputEmail.isNotEmpty() && inputPassword.isNotEmpty(),
                modifier = modifier
                    .padding(start = 16.dp, end = 16.dp, top = 48.dp)
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                Text(
                    text = "Log in",
                    fontSize = 16.sp,
                )
            }
            Text(
                text = "Forgot password?",
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 60.dp)
                    .align(Alignment.CenterHorizontally)
            )
            if (openDialog.value) {
                AlertDialog(
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    title = {
                        if (loginResponse.value) {
                            Text(text = "Success")
                        } else {
                            Text(text = "Error")
                        }

                    },
                    text = {
                        if (loginResponse.value) {
                            Text("Successfully login")
                        } else {
                            Text("Login failed")
                        }
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                if (loginResponse.value) {
                                    openDialog.value = false
//                                    navController.navigate(Screen.Home.route) {
//                                        popUpTo(Screen.Login.route) { saveState = false }
//                                        restoreState = true
//                                        launchSingleTop = true
//                                    }
                                } else {
                                    openDialog.value = false
                                }
                            }
                        ) {
                            Text("OK")
                        }
                    }
                )
            }
        }
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.3f))
            ) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .size(50.dp)
                        .align(Alignment.Center)
                )
            }
        }
    }
}