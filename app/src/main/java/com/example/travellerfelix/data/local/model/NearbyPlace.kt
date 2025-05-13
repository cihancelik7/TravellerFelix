package com.example.travellerfelix.data.local.model

data class NearbyPlace(
    val name: String,
    val distanceInMeters: Double,
    val rating: Float?,
    val lat: Double,
    val lng: Double
)