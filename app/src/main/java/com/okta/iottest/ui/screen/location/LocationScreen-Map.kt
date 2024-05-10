package com.okta.iottest.ui.screen.location

import android.app.Activity
import android.net.Uri
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.BottomStart
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.okta.iottest.R
import com.okta.iottest.ui.components.BottomBar
import com.okta.iottest.ui.components.PeopleStatusRow
import com.okta.iottest.ui.components.createBitmapWithBorder
import com.okta.iottest.ui.theme.Error50
import com.okta.iottest.ui.theme.ErrorContainer
import com.okta.iottest.ui.theme.SemanticBrown10
import androidx.compose.ui.text.font.FontStyle
import coil.compose.rememberImagePainter
import com.google.android.gms.maps.model.Marker
import com.okta.iottest.model.PeopleData
import com.okta.iottest.model.PeopleList
import com.okta.iottest.ui.components.NavigationBottomBar
import com.okta.iottest.ui.theme.OnPrimaryContainer
import com.okta.iottest.ui.theme.PrimaryContainer
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Preview
@Composable
fun MapLocationScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val bottomSheetScaffoldState = rememberBottomSheetScaffoldState()
    val coroutineScope = rememberCoroutineScope()

    val sheetContent = remember { mutableStateOf<@Composable () -> Unit>({}) }
    val isNewSheetContent = remember { mutableStateOf(false) } // Add this line
    val selectedName = remember { mutableStateOf("") } // Add this line
    val selectedLocation = remember { mutableStateOf(LatLng(0.0, 0.0)) }

    val context = LocalContext.current as Activity

    BackHandler {
        context.finish()
    }
    // Update sheetContent to be MySheetContent(sheetContent)
    LaunchedEffect(Unit) {
        sheetContent.value = {
            MySheetContent(
                sheetContent,
                isNewSheetContent,
                selectedName,
                selectedLocation,
            )
        } // Pass isNewSheetContent here
        isNewSheetContent.value = false // And this line
    }


    Scaffold(
        bottomBar = {
            if (isNewSheetContent.value) {
                NavigationBottomBar(navController, selectedLocation = selectedLocation)
            } else {
                BottomBar(navController)
            }
        },
    ) { innerPadding ->
        BottomSheetScaffold(
            sheetContent = {
                sheetContent.value()
            },
            scaffoldState = bottomSheetScaffoldState,
            sheetPeekHeight = 120.dp,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetElevation = 16.dp,
            topBar = {
                if (isNewSheetContent.value) {
                    TopAppBar(
                        title = {
                            Text(
                                "${selectedName.value}'s Location",
                                style = MaterialTheme.typography.titleMedium
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {
                                sheetContent.value = {
                                    MySheetContent(
                                        sheetContent = sheetContent,
                                        isNewSheetContent = isNewSheetContent,
                                        selectedName = selectedName,
                                        selectedLocation = selectedLocation
                                    )
                                    isNewSheetContent.value = false
                                }
                            }) {
                                Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                            }
                        }
                    )
                }
            }
        ) {
            Column(modifier = Modifier.padding(innerPadding)) {
                AndroidView(
                    factory = { context ->
                        val mapView = MapView(context)

                        mapView.getMapAsync { googleMap ->
                            val markerToPeopleData = mutableMapOf<Marker, PeopleData>()
                            PeopleList.forEach { data ->
                                data.location?.let { location -> // Check if location is not null
                                    val bitmapWithBorder = createBitmapWithBorder(
                                        data.profilePicture,
                                        context,
                                        1.5f,
                                        data.statusIcon!!,
                                        data.status,
                                    )
                                    val markerIcon = BitmapDescriptorFactory.fromBitmap(bitmapWithBorder)

                                    val marker = googleMap.addMarker(
                                        MarkerOptions()
                                            .position(location) // Use the non-null location
                                            .title("${data.name} at $location")
                                            .snippet(data.status)
                                            .icon(markerIcon)
                                    )
                                    markerToPeopleData[marker!!] = data
                                }
                            }
                            PeopleList.firstOrNull { it.location != null }?.let { data ->
                                googleMap.moveCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        data.location!!,
                                        15f
                                    )
                                )
                            }

                            googleMap.setOnMarkerClickListener { marker ->
                                val data = markerToPeopleData[marker]
                                if (data != null) {
                                    data.location?.let { location -> // Check if location is not null
                                        googleMap.animateCamera(
                                            CameraUpdateFactory.newLatLngZoom(
                                                location, // Use the non-null location
                                                18f  // Change this value as needed
                                            )
                                        )
                                        coroutineScope.launch {
                                            bottomSheetScaffoldState.bottomSheetState.expand()
                                        }
                                        selectedLocation.value = location // Use the non-null location
                                        sheetContent.value = {
                                            NewSheetContent(
                                                profilePicture = data.profilePicture,
                                                name = data.name,
                                                distance = data.distance,
                                                updatedTime = data.updatedTime,
                                                status = data.status ?: "fine",
                                                isNewSheetContent = isNewSheetContent,
                                                selectedName = selectedName,
                                                location = location, // Use the non-null location
                                                selectedLocation = selectedLocation
                                            )
                                        }
                                    }
                                }
                                true
                            }
                        }
                        mapView
                    },
                    modifier = Modifier.fillMaxSize()
                ) { mapView ->
                    mapView.onCreate(null)
                    mapView.onResume()
                }

            }
        }
    }
}

@Composable
fun MySheetContent(
    sheetContent: MutableState<@Composable () -> Unit>,
    isNewSheetContent: MutableState<Boolean>,
    selectedName: MutableState<String>,
    selectedLocation: MutableState<LatLng>
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
                "People",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(start = 16.dp, bottom = 24.dp, top = 4.dp)
            )
            Column() {
                PeopleList.forEach { data ->
                    data.location?.let { location -> // Check if location is not null
                        PeopleStatusRow(
                            profilePicture = data.profilePicture,
                            name = data.name,
                            distance = data.distance,
                            updatedTime = data.updatedTime,
                            status = data.status,
                            location = location // Use the non-null location
                        ) { profilePicture, name, distance, updatedTime, status, location ->
                            sheetContent.value =
                                {
                                    NewSheetContent(
                                        profilePicture,
                                        name,
                                        distance,
                                        updatedTime,
                                        status,
                                        isNewSheetContent,
                                        selectedName,
                                        location,
                                        selectedLocation
                                    )
                                }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}

@Composable
fun NewSheetContent(
    profilePicture: Any ,
    name: String,
    distance: String,
    updatedTime: String,
    status: String?,
    isNewSheetContent: MutableState<Boolean>,
    selectedName: MutableState<String>,
    location: LatLng,
    selectedLocation: MutableState<LatLng>
) { // Modify this line
    isNewSheetContent.value = true
    selectedName.value = name
    selectedLocation.value = location

    val peopleData = PeopleList.find { it.name == name }

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
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color.White)
                ) {
                    when (profilePicture) {
                        is Int -> {
                            // Load drawable resource
                            Image(painter = painterResource(id = profilePicture), contentDescription = null, modifier = Modifier.clip(RoundedCornerShape(8.dp)).size(60.dp))
                        }
                        is Uri -> {
                            // Load image from Uri
                            Image(painter = rememberImagePainter(data = profilePicture), contentDescription = null, modifier = Modifier.clip(RoundedCornerShape(8.dp)).size(60.dp))
                        }
                    }

                    Column(
                        modifier = Modifier
                            .weight(0.8f)
                            .padding(start = 16.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = name,
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = distance,
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            text = "Last online on $updatedTime",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .weight(0.2f)
                            .align(Alignment.CenterVertically)
                    ) {
                        if (status == "fall") {
                            Image(
                                painter = painterResource(R.drawable.fall_status),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp),
                            )
                        } else if (status == "help") {
                            Image(
                                painter = painterResource(R.drawable.help_icon),
                                contentDescription = null,
                                modifier = Modifier
                                    .size(50.dp),
                            )
                        } else {
                            Box(modifier = Modifier.size(50.dp)) {
                                // No image is shown
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp, bottom = 12.dp),
            ) {
                Text(
                    text = "$name is falling on $updatedTime. Pick $name up now!",
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodySmall,
                    color = Error50,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text = "$name’s Latest Capture",
                    style = MaterialTheme.typography.titleSmall,
                    color = OnPrimaryContainer,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    Box {
                        Image(
                            painter = painterResource(R.drawable.pic_sqp),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(250.dp)
                                .height(125.dp)
                                .align(Center)
                                .clip(RoundedCornerShape(16.dp))
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = "15 April 2024, 9:00 PM",
                            modifier = Modifier
                                .align(BottomStart)
                                .clip(RoundedCornerShape(bottomStart = 16.dp))
                                .background(PrimaryContainer)
                                .padding(4.dp)
                        )
                    }
                    Box {
                        Image(
                            painter = painterResource(R.drawable.pic_prdt),
                            contentDescription = null,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .width(250.dp)
                                .height(125.dp)
                                .align(Center)
                                .clip(RoundedCornerShape(16.dp))
                                .padding(end = 8.dp)
                        )
                        Text(
                            text = "15 April 2024, 9:00 PM",
                            modifier = Modifier
                                .align(BottomStart)
                                .clip(RoundedCornerShape(bottomStart = 16.dp))
                                .background(PrimaryContainer)
                                .padding(4.dp)
                        )
                    }
                }
                Text(
                    text = "$name's History Timeline",
                    style = MaterialTheme.typography.titleSmall,
                    color = OnPrimaryContainer,
                    modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
                )
                Text(
                    text = "Today",
                    style = MaterialTheme.typography.labelMedium,
                    color = OnPrimaryContainer,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                peopleData?.history?.zip(peopleData.historyTime) { location, time ->
                    Row() {
                        Image(
                            painter = painterResource(R.drawable.loc_history_icon),
                            contentDescription = null,
                            modifier = Modifier
                                .weight(0.2f)
                        )
                        Column(
                            modifier = Modifier
                                .weight(0.8f)
                        ) {
                            Text(
                                text = location,
                                style = MaterialTheme.typography.labelMedium,
                                color = OnPrimaryContainer,
                            )
                            Text(
                                text = time,
                                style = MaterialTheme.typography.bodySmall,
                                color = OnPrimaryContainer,
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
