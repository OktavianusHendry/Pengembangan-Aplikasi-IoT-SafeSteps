package com.okta.iottest

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.firestore
import com.google.firebase.messaging.FirebaseMessaging
import com.okta.iottest.model.PeopleList
import com.okta.iottest.model.TestData
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

    private val requestNotificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
//                Toast.makeText(this, "Notifications permission granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Notifications permission rejected", Toast.LENGTH_SHORT).show()
            }
        }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= 33) {
            requestNotificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }

        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w(TAG, "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }
            val token = task.result

            // Log and toast
            Log.d(TAG, "FCM Registration token: $token")
//            Toast.makeText(baseContext, "FCM Registration token: $token", Toast.LENGTH_SHORT).show()
        }

            val notificationChannel= NotificationChannel(
                "notification_channel_id",
                "Help",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager=getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)


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

                    val data = remember { mutableStateOf<TestData?>(null) }
                    LaunchedEffect(Unit) {
                        val db = Firebase.database
                        val ref = db.getReference("test")
                        val firestore = Firebase.firestore
                        val notificationHistoryRef = firestore.collection("notificationHistory")
                        ref.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val newData = snapshot.getValue(TestData::class.java)
                                data.value = newData

                                val budiUser = PeopleList.find { it.name == "Budi" }
                                if (budiUser != null && newData != null) {
                                    val updatedUser = budiUser.copy(
                                        location = LatLng(
                                            newData.location!!.latitude.toDouble(),
                                            newData.location!!.longitude.toDouble()
                                        )
                                    )
                                    PeopleList[PeopleList.indexOf(budiUser)] = updatedUser
                                }

                                if (newData!!.jerk!!.total > 560000f) {
                                    // If so, send a notification
                                    Log.d(TAG, "Budi is falling")
                                    val notification = NotificationCompat.Builder(
                                        this@MainActivity,
                                        "notification_channel_id"
                                    )
                                        .setContentTitle("Help")
                                        .setContentText("Budi is falling")
                                        .setSmallIcon(R.drawable.safesteps_logo)
                                        .setAutoCancel(true)
                                        .build()
                                    notificationManager.notify(1, notification)


                                    // Store notification in Firestore
                                    val notificationData = hashMapOf(
                                        "title" to "Budi has Fallen Recently",
                                        "message" to "Check Budi condition and where Budi is now!",
                                        "timestamp" to FieldValue.serverTimestamp()
                                    )
                                    notificationHistoryRef.add(notificationData)
                                        .addOnSuccessListener { documentReference ->
                                            Log.d(
                                                TAG,
                                                "Notification stored with ID: ${documentReference.id}"
                                            )
                                        }
                                        .addOnFailureListener { e ->
                                            Log.w(TAG, "Error adding notification to history", e)
                                        }
//                                    sendNotification(remoteMessage.notification?.title, "Budi is falling")
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                // Handle error here
                            }
                        })
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
    companion object {
        private const val TAG = "MainActivity"
    }
}