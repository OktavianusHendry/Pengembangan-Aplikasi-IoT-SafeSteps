package com.okta.iottest.model

data class TestData(
    val accelerometer: SensorData? = SensorData(),
    val gyroscope: SensorData? = SensorData(),
    val location: LocationData? = LocationData(),
    val jerk: JerkData? = JerkData()
)

data class SensorData(
    val x: Float = 0.0f,
    val y: Float = 0.0f,
    val z: Float = 0.0f
)

data class LocationData(
    val all: String = "",
    val latitude: String = "",
    val longitude: String = ""
)

data class JerkData(
    val lastFallTime: Float = 0.0f,
    val total : Float = 0.0f
)