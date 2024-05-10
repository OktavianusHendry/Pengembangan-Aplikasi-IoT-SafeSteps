package com.okta.iottest

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.okta.iottest.model.PeopleList
import com.okta.iottest.ui.navigation.Screen
import com.okta.iottest.ui.screen.device.AddDeviceScreen
import com.okta.iottest.ui.screen.device.RegisteredDeviceScreen
import com.okta.iottest.ui.screen.location.MapLocationScreen
import com.okta.iottest.ui.screen.login.LoginScreen
import com.okta.iottest.ui.screen.notification.NotificationScreen
import com.okta.iottest.ui.screen.profile.ProfileScreen
import com.okta.iottest.ui.screen.profile.accsecurity.AccountAndSecurityScreen
import com.okta.iottest.ui.screen.profile.edit.EditProfileScreen
import com.okta.iottest.ui.screen.profile.termscondition.TermsAndConditionScreen
import com.okta.iottest.ui.screen.signup.SignupScreen
import com.okta.iottest.ui.screen.welcome.WelcomeScreen
import com.okta.iottest.ui.theme.IoTtestTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        installSplashScreen()
        setContent {
            IoTtestTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val auth = FirebaseAuth.getInstance()
                    val navController = rememberNavController()
                    val currentUser = remember { mutableStateOf(auth.currentUser) }
                    val startDestination = if (currentUser != null) Screen.Location.route else Screen.Welcome.route

                    auth.addAuthStateListener { firebaseAuth ->
                        if (firebaseAuth.currentUser == null && navController.currentBackStackEntry?.lifecycle?.currentState == Lifecycle.State.RESUMED) {
                            // User is signed out
                            // Navigate to Welcome Screen
                            navController.navigate(Screen.Welcome.route)
                        }
                    }

                    val context = LocalContext.current
                    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

                    if (ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            this,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                            1
                        )
                    } else {
                        viewModel.fetchUserLocation(this)
                    }
                    val userLocation by viewModel.userLocation.observeAsState()
                    userLocation?.let { location ->
                        PeopleList[PeopleList.size - 1] = PeopleList.last().copy(location = location)
                    }


                    BackHandler {
                        when (navController.currentDestination?.route) {
                            Screen.Location.route -> finish()
                            Screen.Welcome.route -> finish()
                            else -> navController.popBackStack()
                        }
                    }

//                    val context = LocalContext.current
                    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
                    val lifecycleOwner = LocalLifecycleOwner.current

                    DisposableEffect(lifecycleOwner) {
                        val observer = LifecycleEventObserver { _, event ->
                            if (event == Lifecycle.Event.ON_RESUME && !cameraPermissionState.status.isGranted) {
                                cameraPermissionState.launchPermissionRequest()
                            }
                        }
                        lifecycleOwner.lifecycle.addObserver(observer)
                        onDispose {
                            lifecycleOwner.lifecycle.removeObserver(observer)
                        }
                    }

                    NavHost(navController = navController, startDestination = startDestination) {
                        composable(Screen.Device.route) {
                            RegisteredDeviceScreen(navController)
                        }
                        composable(Screen.Notification.route) {
                            NotificationScreen(navController)
                        }
                        composable(Screen.Profile.route) {
                            ProfileScreen(navController)
                        }
                        composable(Screen.Location.route) {
                            MapLocationScreen(navController)
                        }
                        composable(Screen.AccountAndSecurity.route){
                            AccountAndSecurityScreen(navController)
                        }
                        composable(Screen.TermsAndCondition.route){
                            TermsAndConditionScreen(navController)
                        }
                        composable(Screen.Welcome.route){
                            WelcomeScreen(navController)
                        }
                        composable(Screen.Login.route){
                            LoginScreen(navController)
                        }
                        composable(Screen.Signup.route){
                            SignupScreen(navController)
                        }
                        composable(Screen.EditProfile.route){
                            EditProfileScreen(navController)
                        }
                        composable(Screen.AddDevice.route){
                            AddDeviceScreen(navController)
                        }
                    }
                }
            }
        }
    }
}