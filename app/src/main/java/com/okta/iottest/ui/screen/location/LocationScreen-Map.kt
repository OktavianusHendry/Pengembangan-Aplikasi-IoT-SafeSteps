package com.okta.iottest.ui.screen.location

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomSheetScaffold
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.rememberBottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterVertically
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.okta.iottest.ui.theme.ErrorContainer
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
//    val markerIcon = FrameMarkerIcon(R.drawable.david_hershey_user)
    val context = LocalContext.current

    Scaffold(
        bottomBar = {
            BottomBar(navController)
        },
    ){innerPadding ->
        BottomSheetScaffold(
            sheetContent = {
                MySheetContent()
            },
            scaffoldState = bottomSheetScaffoldState,
            sheetPeekHeight = 120.dp,
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            sheetElevation = 16.dp,
        ) {
            Column(modifier = Modifier.padding(innerPadding)) {
                AndroidView(
                    factory = { context ->
                        val mapView = MapView(context)
                        mapView.getMapAsync { googleMap ->

                            val scientia = LatLng(-6.255660, 106.615228)
                            val pradita = LatLng(-6.260826, 106.618281)

                            val bitmapWithBorder = createBitmapWithBorder(R.drawable.david_hershey_user, context, 1.5f, R.drawable.fall_status, null)
                            val markerIcon = BitmapDescriptorFactory.fromBitmap(bitmapWithBorder)

                            val marker1 = googleMap.addMarker(
                                MarkerOptions()
                                    .position(scientia)
                                    .title("David Hershey")
                                    .snippet("Mark's location")
                                    .icon(markerIcon)
                            )
                            val bitmapWithBorder2 = createBitmapWithBorder(R.drawable.taylor_user, context, 1.5f, R.drawable.fall_status, "Fall")
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
fun MySheetContent(){
    Spacer(modifier = Modifier.height(12.dp))
    Spacer(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 150.dp)
            .height(4.dp)
            .clip(RoundedCornerShape(50))
            .background(Color.Gray),
    )
    Text(
        "People",
        style = MaterialTheme.typography.titleLarge,
        modifier = Modifier.padding(start = 16.dp, bottom = 24.dp, top = 16.dp)
    )
    PeopleStatusRow(R.drawable.taylor_user, "Taylor Swift", "1.2 km away", "Updated on 09:15 PM", "fall")
    PeopleStatusRow(R.drawable.david_hershey_user, "David Hershey", "5 m away", "Updated on 09:15 PM", "help")
    PeopleStatusRow(R.drawable.delvin_user, "Delvin", "900 m away", "Updated on 09:05 PM", null)
    PeopleStatusRow(R.drawable.player1_user, "Player1", "900 m away", "Updated on 09:05 PM", null )

    Spacer(modifier = Modifier.height(50.dp))
}

