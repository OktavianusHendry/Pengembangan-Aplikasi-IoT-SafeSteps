package com.okta.iottest.ui.screen.location

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Alignment.Companion.Start
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
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
    val context = LocalContext.current

    // Initialize sheetContent with an empty composable
    val sheetContent = remember { mutableStateOf<@Composable () -> Unit>({}) }
    val isNewSheetContent = remember { mutableStateOf(false) } // Add this line

    // Update sheetContent to be MySheetContent(sheetContent)
    LaunchedEffect(Unit) {
        sheetContent.value = { MySheetContent(sheetContent, isNewSheetContent) } // Pass isNewSheetContent here
        isNewSheetContent.value = false // And this line
    }

    Scaffold(
        bottomBar = {
            if (isNewSheetContent.value) {
                NavigationBottomBar(navController)
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
                        title = { Text("David Hershey's Location", style = MaterialTheme.typography.titleMedium) },
                        navigationIcon = {
                            IconButton(onClick = {
                                sheetContent.value = {
                                    MySheetContent(
                                        sheetContent = sheetContent,
                                        isNewSheetContent = isNewSheetContent
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

                            val scientia = LatLng(-6.255660, 106.615228)
                            val pradita = LatLng(-6.260826, 106.618281)

                            val bitmapWithBorder = createBitmapWithBorder(
                                R.drawable.david_hershey_user,
                                context,
                                1.5f,
                                R.drawable.fall_status,
                                null
                            )
                            val markerIcon = BitmapDescriptorFactory.fromBitmap(bitmapWithBorder)

                            val marker1 = googleMap.addMarker(
                                MarkerOptions()
                                    .position(scientia)
                                    .title("David Hershey")
                                    .snippet("Mark's location")
                                    .icon(markerIcon)
                            )
                            val bitmapWithBorder2 = createBitmapWithBorder(
                                R.drawable.taylor_user,
                                context,
                                1.5f,
                                R.drawable.fall_status,
                                "Fall"
                            )
                            val markerIcon2 = BitmapDescriptorFactory.fromBitmap(bitmapWithBorder2)
                            val marker2 = googleMap.addMarker(
                                MarkerOptions()
                                    .position(pradita)
                                    .title("John Doe")
                                    .snippet("My Location")
                                    .icon(markerIcon2)
                            )
                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    pradita,
                                    15f
                                )
                            )

                            // Add an OnMarkerClickListener
                            googleMap.setOnMarkerClickListener { marker ->
                                if (marker == marker1 || marker == marker2) {
                                    // Zoom in to the marker
                                    googleMap.animateCamera(
                                        CameraUpdateFactory.newLatLngZoom(
                                            marker.position,
                                            18f  // Change this value as needed
                                        )
                                    )

                                    // Open the modal sheet
                                    // You need to have a reference to your BottomSheetScaffoldState
                                    coroutineScope.launch {
                                        bottomSheetScaffoldState.bottomSheetState.expand()
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
fun MySheetContent(sheetContent: MutableState<@Composable () -> Unit>, isNewSheetContent: MutableState<Boolean>) {
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
            Column(){
                PeopleStatusRow(
                    R.drawable.taylor_user,
                    "Taylor Swift",
                    "1.2 km away",
                    "Updated on 09:15 PM",
                    status = "fall",
                    onClick = { status ->
                        // Update the sheetContent state when this row is clicked
                        sheetContent.value = { NewSheetContent(status, isNewSheetContent) }
                    }
                )
                PeopleStatusRow(
                    R.drawable.david_hershey_user,
                    "David Hershey",
                    "5 m away",
                    "Updated on 09:15 PM",
                    "help"
                )
                PeopleStatusRow(R.drawable.delvin_user, "Delvin", "900 m away", "Updated on 09:05 PM", null)
                PeopleStatusRow(R.drawable.player1_user, "Player1", "900 m away", "Updated on 09:05 PM", null)

            }

            Spacer(modifier = Modifier.height(50.dp))
        }
    }


}

@Composable
fun NewSheetContent(status: String, isNewSheetContent: MutableState<Boolean>)  { // Modify this line
    isNewSheetContent.value = true
    // This is the new content that will be shown when a PeopleStatusRow is clicked
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
                ){
                    Image(
                        painter = painterResource(R.drawable.david_hershey_user),
                        contentDescription = null,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .size(60.dp)
                    )
                    Column(
                        modifier = Modifier
                            .weight(0.7f)
                            .padding(start = 16.dp),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.Top
                    ) {
                        Text(
                            text = "David Hershey",
                            style = MaterialTheme.typography.titleMedium,
                        )
                        Text(
                            text = "750m ahead from You",
                            style = MaterialTheme.typography.bodySmall,
                        )
                        Text(
                            text = "Updated on 09:15 PM",
                            style = MaterialTheme.typography.bodySmall,
                        )
                    }
                    Row(
                        horizontalArrangement = Arrangement.End,
                        modifier = Modifier
                            .weight(0.3f)
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
                    text = "David is falling on 9:10 PM. Pick David up now!",
                    fontStyle = FontStyle.Italic,
                    style = MaterialTheme.typography.bodySmall,
                    color = Error50,
                    modifier = Modifier.padding(bottom = 12.dp)
                )
                Text(
                    text ="David’s Latest Capture",
                    style = MaterialTheme.typography.titleSmall,
                    color = OnPrimaryContainer,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState())
                ) {
                    Box {
                        Image(
                            painter = painterResource(R.drawable.foto1),
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
                            painter = painterResource(R.drawable.foto1),
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
                    text ="David’s History Timeline",
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
                            text = "Scientia Square Park",
                            style = MaterialTheme.typography.labelMedium,
                            color = OnPrimaryContainer,
                        )
                        Text(
                            text = "8:00 PM - Now",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnPrimaryContainer,
                        )
                    }
                }
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
                            text = "Pradita University",
                            style = MaterialTheme.typography.labelMedium,
                            color = OnPrimaryContainer,
//                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        Text(
                            text = "5:00 PM - 8:00 PM",
                            style = MaterialTheme.typography.bodySmall,
                            color = OnPrimaryContainer,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(50.dp))
        }
    }
}
