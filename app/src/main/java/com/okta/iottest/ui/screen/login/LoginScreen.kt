package com.okta.iottest.ui.screen.login

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.okta.iottest.R
import com.okta.iottest.ui.components.EmailText
import com.okta.iottest.ui.components.PasswordText
import com.okta.iottest.ui.navigation.Screen
import com.okta.iottest.ui.screen.profile.GoogleSignInHelper
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@Preview
@Composable
fun LoginScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val viewModel: LoginViewModel = viewModel()
    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val loginResponse by viewModel.loginResponse.observeAsState(initial = false)
    val errorMessage by viewModel.errorMessage.observeAsState(initial = null)

    var inputEmail by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var inputPassword by remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current
    val oneTapClient = remember {
        Identity.getSignInClient(context)
    }

    val googleSignInHelper = remember {
        GoogleSignInHelper(
            auth = auth,
            context = context,
            oneTapClient = oneTapClient
        )
    }
    val coroutineScope = rememberCoroutineScope()

    val signInWithGoogleLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                googleSignInHelper.signInWithIntent(
                    result.data ?: return@rememberLauncherForActivityResult
                )
                    .onEach { signInResult ->
                        if (signInResult.isSuccess) {
                            // Navigate to Location Screen
                            navController.navigate(Screen.Location.route) {
                                popUpTo(Screen.Login.route) { saveState = false }
                                restoreState = true
                                launchSingleTop = true
                            }
                        }
                    }
                    .launchIn(coroutineScope)
            }
        }
    )


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
                    viewModel.signInWithEmailAndPassword(auth, inputEmail, inputPassword)
                },
                enabled = isEmailValid && inputEmail.isNotEmpty() && inputPassword.isNotEmpty(),
                modifier = modifier
                    .padding(start = 16.dp, end = 16.dp, top = 48.dp)
                    .fillMaxWidth()
                    .height(64.dp)
            ){
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
            Button(
                onClick = {
                    googleSignInHelper.createIntent {
                        signInWithGoogleLauncher.launch(
                            IntentSenderRequest.Builder(
                                it.intentSender
                            ).build()
                        )
                    }
                },
                modifier = modifier
                    .padding(start = 16.dp, end = 16.dp, top = 48.dp)
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                Text(
                    text = "Sign in with Google",
                    fontSize = 16.sp,
                )
            }
            if (loginResponse) {
                AlertDialog(
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    title = {
                        Text(text = "Success")
                    },
                    text = {
                        Text("Successfully login")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                openDialog.value = false
                                navController.navigate(Screen.Location.route) {
                                    popUpTo(Screen.Login.route) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            }
                        ) {
                            Text("OK")
                        }
                    }
                )
            } else if (errorMessage != null) {
                AlertDialog(
                    onDismissRequest = {
                        openDialog.value = false
                    },
                    title = {
                        Text(text = "Error")
                    },
                    text = {
                        Text(errorMessage ?: "Login failed")
                    },
                    confirmButton = {
                        Button(
                            onClick = {
                                openDialog.value = false
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


