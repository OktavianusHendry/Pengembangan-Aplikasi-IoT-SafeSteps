package com.okta.iottest.ui.screen.location

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.okta.iottest.R
import com.okta.iottest.ui.components.BottomBar

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Preview
@Composable
fun MapLocationScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    Scaffold(
        bottomBar = {
            BottomBar(navController)
        },
    ){innerPadding ->
        BottomSheetScaffold(
            sheetContent = {
                MySheetContent()
            },
            scaffoldState = rememberBottomSheetScaffoldState(),
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
                            googleMap.addMarker(
                                MarkerOptions()
                                    .position(scientia)
                                    .title("Scientia Square Park")
                                    .snippet("Marker in Scientia Square Park")
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker))
                            )
                            googleMap.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    scientia,
                                    15f
                                )
                            )
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
        modifier = Modifier.padding(16.dp)
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 16.dp, top = 8.dp, end = 16.dp, bottom = 32.dp),
    ) {
        Image(
            painter = painterResource(R.drawable.userdefault),
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "John Doe (Me)",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(
                text = "Updated on 09:15 PM",
                style = MaterialTheme.typography.bodySmall,
            )
        }
    }
    Spacer(modifier = Modifier.height(50.dp))
}