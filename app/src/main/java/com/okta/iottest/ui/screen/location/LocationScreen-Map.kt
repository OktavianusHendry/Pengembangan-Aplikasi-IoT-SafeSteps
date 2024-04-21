package com.okta.iottest.ui.screen.location

import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberSwipeableState
import androidx.compose.material.swipeable
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
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
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.okta.iottest.R
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Preview
@Composable
fun MapLocationScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    var showBottomSheet by remember { mutableStateOf(false) }

        Scaffold(
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        showBottomSheet = true
                    },
                    backgroundColor = MaterialTheme.colorScheme.primary,
                ) {
                    Icon(Icons.Filled.KeyboardArrowUp, contentDescription = "", tint = Color.White)
                }

            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
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
                if (showBottomSheet) {
                    ModalBottomSheet(
                        onDismissRequest = {
                            showBottomSheet = false
                        },
                    ) {
                        // Sheet content
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
                    }
                }
            }
        }
}
