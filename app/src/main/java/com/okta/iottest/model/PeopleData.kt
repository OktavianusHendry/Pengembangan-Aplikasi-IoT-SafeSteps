package com.okta.iottest.model

import com.google.android.gms.maps.model.LatLng
import com.okta.iottest.R

data class PeopleData(
    val profilePicture: Int,
    val name: String,
    val distance: String,
    val updatedTime: String,
    val status: String?,
    val statusIcon: Int, // Add this line
    val location: LatLng
)

val PeopleList = listOf(
    PeopleData(
        profilePicture = R.drawable.taylor_user,
        name = "Taylor Swift",
        distance = "1.2 km away",
        updatedTime = "Updated on 09:15 PM",
        status = "fall",
        statusIcon = R.drawable.fall_status,
        location = LatLng(-6.255660, 106.615228)
    ),
    PeopleData(
        R.drawable.david_hershey_user,
        name = "David Hershey",
        distance = "5 m away",
        updatedTime = "Updated on 09:15 PM",
        status = "help",
        statusIcon = R.drawable.help_icon,
        location = LatLng(-6.260826, 106.618281)
    ),
    PeopleData(
        R.drawable.delvin_user,
        name = "Delvin",
        distance = "1.2 km away",
        updatedTime = "Updated on 09:15 PM",
        status = null,
        statusIcon = R.drawable.fall_status,
        location = LatLng(-6.275180, 106.613830)
    ),
)