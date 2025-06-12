package com.example.travellerfelix.data.remote.model

import androidx.room.Entity
import com.google.gson.annotations.SerializedName

data class NearbyPlaceResponse(
    @SerializedName("results")
    val places: List<Place>
)
@Entity(tableName = "place")
data class Place(
    @SerializedName("name")
    val name: String?,
    @SerializedName("vicinity")
    val address: String?,
    @SerializedName("geometry")
    val geometry: Geometry?,
    @SerializedName("rating")
    val rating: Double?,
    @SerializedName("user_ratings_total")
    val userRatingsTotal: Int?
)
data class Geometry(
    @SerializedName("location")
    val location: Location?
)
data class Location(
    @SerializedName("lat")
    val lat: Double?,
    @SerializedName("lng")
    val lng: Double?
)