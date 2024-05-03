package com.okta.iottest.ui.screen.profile.edit

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.net.Uri
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.okta.iottest.R
import com.okta.iottest.ui.components.BottomBar
import com.okta.iottest.ui.components.PicturePicker
import com.okta.iottest.ui.navigation.Screen
import com.okta.iottest.ui.screen.profile.ProfileViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    uri: Uri? = null, //target url to preview
    directory: File? = null, // stored directory
    onSetUri : (Uri) -> Unit = {}, // selected / taken uri
) {
    val viewModel: EditProfileViewModel = viewModel()
    val email by viewModel.email.collectAsState()
    val name by viewModel.name.collectAsState()
    val photoUrl by viewModel.photoUrl.collectAsState()
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val updateResponse = remember { mutableStateOf(false) }
    var updateAttempted by remember { mutableStateOf<Boolean?>(null) }

    val tempUri = remember { mutableStateOf<Uri?>(null) }
    val authority = stringResource(id = R.string.fileprovider)

    // for takePhotoLauncher used
    fun getTempUri(): Uri? {
        directory?.let {
            it.mkdirs()
            val file = File.createTempFile(
                "image_" + System.currentTimeMillis().toString(),
                ".jpg",
                it
            )

            return FileProvider.getUriForFile(
                context,
                authority,
                file
            )
        }
        return null
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            it?.let {
                onSetUri.invoke(it)
            }
        }
    )

    val takePhotoLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = {isSaved ->
            tempUri.value?.let {
                onSetUri.invoke(it)
            }
        }
    )

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permission is granted, launch takePhotoLauncher
            val tmpUri = getTempUri()
            tempUri.value = tmpUri
            takePhotoLauncher.launch(tempUri.value)
        } else {
            // Permission is denied, handle it accordingly
        }
    }
    var showPicturePicker by remember { mutableStateOf(false) }
    if (showPicturePicker){
        PicturePicker(
            onDismiss = {
                showPicturePicker = false
            },
            onTakePhotoClick = {
                showPicturePicker = false

                val permission = Manifest.permission.CAMERA
                if (ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED
                ) {
                    // Permission is already granted, proceed to step 2
                    val tmpUri = getTempUri()
                    tempUri.value = tmpUri
                    tmpUri?.let {
                        takePhotoLauncher.launch(it)
                    }
                } else {
                    // Permission is not granted, request it
                    cameraPermissionLauncher.launch(permission)
                }
            },
            onPhotoGalleryClick = {
                showPicturePicker = false
                imagePicker.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            },
        )
    }

    Column (
        modifier = Modifier.fillMaxWidth()
    ) {

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = {
                    showPicturePicker = true
                }
            ) {
                Text(text = "Select / Take")
            }
        }

        //preview selfie


    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Profile") },
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
                .fillMaxSize() // Fill the entire screen
                .wrapContentSize(Alignment.Center)
                .padding(top = 32.dp, start = 16.dp, end = 16.dp), // Center the content
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier,
            ) {
                Image(
                    painter = rememberImagePainter(data = photoUrl, builder = {
                        placeholder(R.drawable.welcome5)
                    }),
                    contentDescription = null,
                    modifier = modifier
                        .size(85.dp)
                        .clip(shape = CircleShape)
                        .aspectRatio(1f)
                )
                Column(Modifier.fillMaxWidth()) {
                    uri?.let {
                        Box(
                            modifier = Modifier.fillMaxWidth(),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = it,
                                modifier = Modifier.size(
                                    160.dp
                                ),
                                contentDescription = null,
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                Text(
                    text = "Edit Profile Photo",
                    style = MaterialTheme.typography.labelLarge,
                    modifier = Modifier.clickable {
                        showPicturePicker = true
                    }
                )
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = "Nama Lengkap",
                    modifier = Modifier.align(Alignment.Start)
                )
                OutlinedTextField(
                    value = name ?: "",
                    onValueChange = { newName ->
                        viewModel.updateName(newName)
                    },
                    maxLines = 1,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Next),
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(16.dp))
                Text(
                    text = "Email",
                    modifier = Modifier.align(Alignment.Start)
                )
                OutlinedTextField(
                    value = email ?: "",
                    onValueChange = { newEmail ->
                        viewModel.updateEmail(newEmail)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next),
                    maxLines = 1,
                    modifier = Modifier
                        .fillMaxWidth()
                )
                Spacer(modifier = Modifier.padding(16.dp))
                Button(
                    onClick = {
                        val user = auth.currentUser
                        val profileUpdates = UserProfileChangeRequest.Builder()
                            .setDisplayName(name)
                            .build()

                        user?.updateProfile(profileUpdates)?.addOnCompleteListener { updateTask ->
                            if (updateTask.isSuccessful) {
                                Log.d(ContentValues.TAG, "User profile updated.")
                                updateResponse.value = true
                                updateAttempted = true
                            } else {
                                // Profile update failed
                                Log.w(ContentValues.TAG, "updateProfile:failure", updateTask.exception)
                                updateResponse.value = false
                            }
                        }
                    },
                    modifier = Modifier
                        .padding(top = 12.dp,)
                        .fillMaxWidth()
                        .height(48.dp)
                ) {
                    Text("Save Changes")
                }

                if (updateAttempted != null){
                    LaunchedEffect(updateResponse.value) {
                        if (updateResponse.value) {
                            Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT).show()
                            navController.navigate(Screen.Location.route) {
                                popUpTo(Screen.EditProfile.route) {
                                    inclusive = true
                                }
                                launchSingleTop = true
                            }
                        } else  {
                            Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}


