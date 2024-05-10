package com.okta.iottest

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng

class MainViewModel : ViewModel() {
    private val _userLocation = MutableLiveData<LatLng>()
    val userLocation: LiveData<LatLng> = _userLocation

    @SuppressLint("MissingPermission")
    fun fetchUserLocation(context: Context) {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener { location: Location? ->
            location?.let {
                _userLocation.value = LatLng(it.latitude, it.longitude)
            }
        }
    }
}
