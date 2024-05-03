package com.okta.iottest.ui.screen.profile.edit

import android.Manifest
import android.content.ContentValues
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
import com.okta.iottest.ui.components.PicturePicker
import com.okta.iottest.ui.navigation.Screen
import com.okta.iottest.ui.screen.location.MySheetContent
import com.okta.iottest.ui.theme.OnPrimary100
import com.okta.iottest.ui.theme.OnPrimaryContainer
import com.okta.iottest.ui.theme.Primary70
import kotlinx.coroutines.launch
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun EditProfileScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val viewModel: EditProfileViewModel = viewModel()
    val email by viewModel.email.collectAsState()
    val name by viewModel.name.collectAsState()
    val photoUrl by viewModel.photoUrl.collectAsState()
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()
    val updateResponse = remember { mutableStateOf(false) }
    var updateAttempted by remember { mutableStateOf<Boolean?>(null) }

    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val coroutineScope = rememberCoroutineScope()
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
        }

    val cameraLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            // Convert the Bitmap to a Uri and assign it to imageUri
            imageUri = bitmap?.let { getImageUri(context, it) }
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
        BottomSheetScaffold(
            sheetContent = {
                SelectPictureOptionSheet(launcher, cameraLauncher)
            },
            scaffoldState = bottomSheetScaffoldState,
            sheetPeekHeight = 0.dp,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetElevation = 16.dp,
        ) {
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
                    if (imageUri != null) {
                        Image(
                            painter = rememberImagePainter(data = imageUri),
                            contentDescription = "Selected Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(85.dp)
                                .clip(shape = CircleShape)
                                .aspectRatio(1f)
                                .border(2.dp, Primary70, CircleShape)
                        )
                    } else {
                        Image(
                            painter = rememberImagePainter(data = photoUrl, builder = {
                                placeholder(R.drawable.welcome5)
                            }),
                            contentDescription = null,
                            modifier = Modifier
                                .size(85.dp)
                                .clip(shape = CircleShape)
                                .aspectRatio(1f)
                                .border(2.dp, Primary70, CircleShape)
                        )
                    }
                    Text(
                        text = "Edit Profile Photo",
                        style = MaterialTheme.typography.labelLarge,
                        modifier = Modifier.clickable {
                            coroutineScope.launch {
                                bottomSheetScaffoldState.bottomSheetState.expand()
                            }
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
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
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
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.padding(16.dp))
                    Button(
                        onClick = {
                            val user = auth.currentUser

                            if (imageUri != null) {
                                val compressedImageUri = compressImageFile(
                                    context,
                                    imageUri!!,
                                    80
                                ) // Compress the image to 80% quality matiin klo trnyta kuat

                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .setPhotoUri(compressedImageUri)
                                    .build()

                                user?.updateProfile(profileUpdates)
                                    ?.addOnCompleteListener { updateTask ->
                                        if (updateTask.isSuccessful) {
                                            Log.d(ContentValues.TAG, "User profile updated.")
                                            updateResponse.value = true
                                            updateAttempted = true
                                        } else {
                                            // Profile update failed
                                            Log.w(
                                                ContentValues.TAG,
                                                "updateProfile:failure",
                                                updateTask.exception
                                            )
                                            Toast.makeText(
                                                context,
                                                "Failed to update profile: ${updateTask.exception?.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            updateResponse.value = false
                                        }
                                    }
                            } else {
                                val profileUpdates = UserProfileChangeRequest.Builder()
                                    .setDisplayName(name)
                                    .build()

                                user?.updateProfile(profileUpdates)
                                    ?.addOnCompleteListener { updateTask ->
                                        if (updateTask.isSuccessful) {
                                            Log.d(ContentValues.TAG, "User profile updated.")
                                            updateResponse.value = true
                                            updateAttempted = true
                                        } else {
                                            // Profile update failed
                                            Log.w(
                                                ContentValues.TAG,
                                                "updateProfile:failure",
                                                updateTask.exception
                                            )
                                            Toast.makeText(
                                                context,
                                                "Failed to update profile: ${updateTask.exception?.message}",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            updateResponse.value = false
                                        }
                                    }
                            }
//
//                        user?.updateProfile(profileUpdates)?.addOnCompleteListener { updateTask ->
//                            if (updateTask.isSuccessful) {
//                                Log.d(ContentValues.TAG, "User profile updated.")
//                                updateResponse.value = true
//                                updateAttempted = true
//                            } else {
//                                // Profile update failed
//                                Log.w(ContentValues.TAG, "updateProfile:failure", updateTask.exception)
//                                Toast.makeText(context, "Failed to update profile: ${updateTask.exception?.message}", Toast.LENGTH_LONG).show()
//                                updateResponse.value = false
//                            }
//                        }
                        },
                        modifier = Modifier
                            .padding(top = 12.dp)
                            .fillMaxWidth()
                            .height(48.dp)
                    ) {
                        Text("Save Changes")
                    }

                    if (updateAttempted != null) {
                        LaunchedEffect(updateResponse.value) {
                            if (updateResponse.value) {
                                Toast.makeText(context, "Successfully Updated", Toast.LENGTH_SHORT)
                                    .show()
                                navController.navigate(Screen.Location.route) {
                                    popUpTo(Screen.EditProfile.route) {
                                        inclusive = true
                                    }
                                    launchSingleTop = true
                                }
                            } else {
                                Toast.makeText(context, "Update failed", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SelectPictureOptionSheet(
    launcher: ActivityResultLauncher<String>,
    cameraLauncher: ActivityResultLauncher<Void?>
) {
    Column(modifier = Modifier.heightIn(min = 100.dp, max = 350.dp)) {
        Spacer(modifier = Modifier.height(12.dp))
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 150.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.Gray),
        )
        Spacer(modifier = Modifier.height(12.dp))
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Text(
                "Select Profile Picture",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp, bottom = 16.dp, top = 4.dp)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Button(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 8.dp),
                    onClick = { cameraLauncher.launch(null) }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_photo_camera_24),
                        contentDescription = null,
                        tint = OnPrimary100,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text ="Camera")
                }
                Button(
                    modifier = Modifier
                        .weight(0.5f)
                        .padding(end = 8.dp),
                    onClick = {  launcher.launch("image/*") }
                ) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.baseline_insert_photo_24),
                        contentDescription = null,
                        tint = OnPrimary100,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(text ="Gallery")
                }
//                Row(modifier = Modifier
//                    .fillMaxWidth()
//                    .align(Alignment.CenterHorizontally)
//                    .padding(vertical = 8.dp)
//                    .clickable { }
//                ) {
//                    Image(painter = painterResource(R.drawable.baseline_photo_camera_24), contentDescription =null )
//                    Text(text = "Camera", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(start = 16.dp))
//                }
//                Row(modifier = Modifier
//                    .fillMaxWidth()
//                    .align(Alignment.CenterHorizontally)
//                    .padding(vertical = 8.dp)
//                    .clickable { }
//                ) {
//                    Image(painter = painterResource(R.drawable.baseline_insert_photo_24), contentDescription =null )
//                    Text(text = "Gallery", style = MaterialTheme.typography.labelLarge, modifier = Modifier.padding(start = 16.dp))
//                }
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

fun compressImageFile(context: Context, imageUri: Uri, quality: Int): Uri? {
    val bitmap = BitmapFactory.decodeStream(context.contentResolver.openInputStream(imageUri))

    // Create a new file in the app's cache directory
    val file = File(context.cacheDir, "compressed_image.jpg")
    val outStream = FileOutputStream(file)

    // Compress the bitmap and write it to the new file
    bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outStream)

    // Close the output stream
    outStream.flush()
    outStream.close()

    // Return the URI of the new file
    return Uri.fromFile(file)
}

fun getImageUri(inContext: Context, inImage: Bitmap): Uri {
    val bytes = ByteArrayOutputStream()
    inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
    val path =
        MediaStore.Images.Media.insertImage(inContext.contentResolver, inImage, "Title", null)
    return Uri.parse(path)
}