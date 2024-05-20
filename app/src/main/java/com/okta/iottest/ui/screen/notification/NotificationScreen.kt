package com.okta.iottest.ui.screen.notification

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.firebase.Firebase
import com.google.firebase.Timestamp
import com.google.firebase.database.database
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.firestore
import com.okta.iottest.R
import com.okta.iottest.model.NotificationData
import com.okta.iottest.model.TestData
import com.okta.iottest.ui.components.BottomBar
import com.okta.iottest.ui.navigation.Screen
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Preview
@Composable
fun NotificationScreen(
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier,
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val data = remember { mutableStateOf<TestData?>(null) }
    val notificationHistory = remember { mutableStateOf<List<NotificationData>>(listOf()) }
    LaunchedEffect(Unit) {
        val db = Firebase.database
        val ref = db.getReference("test")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                data.value = snapshot.getValue(TestData::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error here
            }
        })

        val firestore = Firebase.firestore
        firestore.collection("notificationHistory")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .addOnSuccessListener { querySnapshot ->
                val notifications = querySnapshot.documents.mapNotNull { document ->
                    document.toObject(NotificationData::class.java)
                }
                notificationHistory.value = notifications
            }
            .addOnFailureListener { exception ->
                Log.w(TAG, "Error getting notification history: ", exception)
            }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notification") },
                navigationIcon = {},
                actions = {
                    // Add action icons here
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.onPrimary)
            )
        },
        bottomBar = {
//            if (currentRoute != Screen.DetailReward.route) { //Change
            BottomBar(navController)
//            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {

            notificationHistory.value.forEach { notification ->
            Row {

                Row (modifier = Modifier.padding(horizontal = 16.dp).height(100.dp)){
                        // Display each notification here
                        Box (modifier = Modifier.size(70.dp)){
                            Image(
                                painter = painterResource(id = R.drawable.user_budi),
                                contentDescription = null,
                                modifier = Modifier.size(65.dp)
                            )
                            Image(
                                painter = painterResource(id = R.drawable.fall_status),
                                contentDescription = "Notification Icon",
                                modifier = Modifier
//                                .padding(16.dp)
                                    .align(Alignment.BottomEnd)
                            )
                        }
                        Column (modifier = Modifier.padding(start = 8.dp)){
                            Text(
                                text ="${notification.title}",
                                 style = TextStyle(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp
                                ),

//                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Text(
                                text = "${notification.message}",
                                style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp
                                ),
                                modifier = Modifier.padding(top = 8.dp)
                            )
                            Text(
                                text = formatTimestamp(notification.timestamp?: Timestamp.now()),
                                style = TextStyle(
                                    fontWeight = FontWeight.Normal,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                ),
                                modifier = Modifier.padding(top = 8.dp)
                            )

                        }
                    }

                }
            }
        }
    }
}
fun formatTimestamp(timestamp: Timestamp): String {
    val sdf = SimpleDateFormat("h:mm a", Locale.getDefault())
    return sdf.format(timestamp.toDate())
}