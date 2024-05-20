package com.okta.iottest.model

import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.auth.FirebaseAuth
import com.okta.iottest.R

data class PeopleData(
    val profilePicture: Any,
    val name: String,
    val distance: String,
    val updatedTime: String,
    val status: String?,
    val statusIcon: Int, // Add this line
    val location: LatLng?,
    val history: List<String>,
    val historyTime: List<String>
)
val user = FirebaseAuth.getInstance().currentUser
val userName = user?.displayName ?: "Unknown"
val userPhotoUrl = user?.photoUrl ?: "Unknown"

val PeopleList = mutableListOf(
    PeopleData(
        profilePicture = R.drawable.user_dimas,
        name = "Dimas",
        distance = "1.2 km away",
        updatedTime = "09:15 PM",
        status = "fall",
        statusIcon = R.drawable.fall_status,
        location = LatLng(-6.255660, 106.615228),
        history = listOf("Pradita University", "Scientia Square Park", "Scientia Digital Center"),
        historyTime = listOf("03:00 PM - Now", "12:00 PM - 03:00 PM", "09:00 AM - 12:00 PM")
    ),
    PeopleData(
        R.drawable.user_budi,
        name = "Budi",
        distance = "5 m away",
        updatedTime = "09:15 PM",
        status = "help",
        statusIcon = R.drawable.help_icon,
        location = null,
        history = listOf("Pradita University", "Scientia Square Park"),
        historyTime = listOf("03:00 PM - Now", "12:00 PM - 03:00 PM")
    ),
    PeopleData(
        R.drawable.user_siti,
        name = "Siti",
        distance = "2.7 km away",
        updatedTime = "09:15 PM",
        status = null,
        statusIcon = R.drawable.status_null,
        location = LatLng(-6.275180, 106.613830),
        history = listOf("Pradita University"),
        historyTime = listOf("03:00 PM - Now")
    ),
    PeopleData(
        profilePicture = userPhotoUrl ?: R.drawable.baseline_person_24,
        name = userName,
        distance = "0 m",
        updatedTime = "Now",
        status = null,
        statusIcon = R.drawable.status_null,
        location = null, // You might want to replace this with the user's actual location
        history = listOf("Unknown"),
        historyTime = listOf("Unknown")
    )
)