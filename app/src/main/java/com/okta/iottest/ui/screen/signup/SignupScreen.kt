package com.okta.iottest.ui.screen.signup

import android.content.ContentValues.TAG
import android.util.Log
import android.util.Patterns
import android.widget.Toast
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.okta.iottest.R
import com.okta.iottest.ui.components.EmailText
import com.okta.iottest.ui.components.PasswordText
import com.okta.iottest.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun SignupScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    var isLoading by remember { mutableStateOf(false) }

    var inputEmail by remember { mutableStateOf("") }
    var inputNama by remember { mutableStateOf("") }
    var isEmailValid by remember { mutableStateOf(true) }
    var inputPassword by remember { mutableStateOf("") }
    var inputConfirmPassword by remember { mutableStateOf("") }
    var isPasswordTextTapped by remember { mutableStateOf(false) }
    var isConfirmPasswordTextTapped by remember { mutableStateOf(false) }
    val isPasswordValid = inputPassword.length >= 8
    val isConfirmPasswordValid = inputConfirmPassword.length >= 8
    val openDialog = remember { mutableStateOf(false) }
    val signupResponse = remember { mutableStateOf(false) }
    val auth = FirebaseAuth.getInstance()
//    val snackbarHostState = remember { SnackbarHostState() }
    var signupAttempted by remember { mutableStateOf<Boolean?>(null) }
    val context = LocalContext.current

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
                text = "Register",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 8.dp)
            )
            Text(
                text = "Make your account and secure your loved one life",
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            )
            Text(
                text = "Full Name",
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 16.dp, end = 32.dp, top = 28.dp, bottom = 8.dp)
            )
            OutlinedTextField(
                value = inputNama,
                onValueChange = { newName ->
                    inputNama = newName
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                placeholder = { Text("Ex: John Doe") },
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp)
            )
            Text(
                text = "Email",
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top=16.dp, bottom = 8.dp)
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
                    .padding(start = 16.dp, end = 16.dp, top=8.dp, bottom = 8.dp)
            )
            PasswordText(
                input = inputPassword,
                onValueChange = {
                    inputPassword = it
                    isPasswordTextTapped = true
                },
                isError = !isPasswordValid,
                isTapped = isPasswordTextTapped
            )
            Text(
                text = "Confirm Password",
                fontSize = 14.sp,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top=16.dp, bottom = 8.dp)
            )
            PasswordText(
                input = inputConfirmPassword,
                onValueChange = {
                    inputConfirmPassword = it
                    isConfirmPasswordTextTapped = true
                },
                isError = !isConfirmPasswordValid,
                isTapped = isConfirmPasswordTextTapped
            )
            Button(
                onClick = {
                    isLoading = true
                    auth.createUserWithEmailAndPassword(inputEmail, inputPassword)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                // User is successfully registered and logged in
                                val user = auth.currentUser
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(inputNama)
                                    .build()

                                user?.updateProfile(profileUpdates)?.addOnCompleteListener { updateTask ->
                                    isLoading = false
                                    if (updateTask.isSuccessful) {
                                        Log.d(TAG, "User profile updated.")
                                        openDialog.value = true
                                        signupResponse.value = true
                                        signupAttempted = true
                                    } else {
                                        // Profile update failed
                                        Log.w(TAG, "updateProfile:failure", updateTask.exception)
                                        openDialog.value = true
                                        signupResponse.value = false
                                    }
                                }

                            } else {
                                // Registration failed
                                isLoading = false
                                Log.w(TAG, "createUserWithEmail:failure", task.exception)
                                openDialog.value = true
                                signupResponse.value = false
                            }
                        }
                },
                enabled = inputNama.isNotEmpty() && isEmailValid && inputEmail.isNotEmpty() && inputPassword.isNotEmpty() && inputConfirmPassword.isNotEmpty() && inputPassword == inputConfirmPassword,
                modifier = Modifier
                    .padding(start = 16.dp, end = 16.dp, top = 36.dp)
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                Text(
                    text = "Sign up",
                    fontSize = 16.sp,
                )
            }

//            LaunchedEffect(signupResponse.value) {
//                if (signupResponse.value) {
//                    navController.navigate(Screen.Location.route) {
//                        popUpTo(Screen.Signup.route) {
//                            inclusive = true
//                        }
//                        launchSingleTop = true
//                    }
//                }
//            }
            if (signupAttempted != null) {
                LaunchedEffect(signupResponse.value) {
                    if (signupResponse.value) {
                        Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT).show()
//                        snackbarHostState.showSnackbar(
//                            message = "Successfully registered",
//                            actionLabel = "Continue",
//                            duration = SnackbarDuration.Short
//                        )

                        signupAttempted = null  // Reset to null after showing the Snackbar
                        navController.navigate(Screen.Location.route) {
                            popUpTo(Screen.Signup.route) {
                                inclusive = true
                            }
                            launchSingleTop = true
                        }
                    } else {
                        Toast.makeText(context, "Registration failed", Toast.LENGTH_SHORT).show()
//                        snackbarHostState.showSnackbar(
//                            message = "Registration failed",
//                            actionLabel = "Retry",
//                            duration = SnackbarDuration.Short
//                        )
                        signupAttempted = null  // Reset to null after showing the Snackbar
                    }
                }
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