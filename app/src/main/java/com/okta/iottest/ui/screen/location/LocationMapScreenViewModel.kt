package com.okta.iottest.ui.screen.location

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.android.gms.maps.model.LatLng
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import com.okta.iottest.model.PeopleData
import com.okta.iottest.model.TestData

class LocationMapScreenViewModel: ViewModel() {
    val peopleList = mutableStateListOf<PeopleData>()

    init {
        val db = Firebase.database
        val ref = db.getReference("test")
        ref.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val data = snapshot.getValue(TestData::class.java)
                updatePeople(data!!)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error here
            }
        })
    }

    // Function to update peopleList
    fun updatePeople(data: TestData) {
        val user = peopleList.find { it.name == "Budi" }
        if (user != null) {
            val updatedUser = user.copy(location = LatLng(data.location!!.latitude.toDouble(), data.location!!.longitude.toDouble()))
            peopleList[peopleList.indexOf(user)] = updatedUser
        }
    }

}