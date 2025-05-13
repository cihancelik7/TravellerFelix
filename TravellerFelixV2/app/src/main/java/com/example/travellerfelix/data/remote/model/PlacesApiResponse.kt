package com.example.travellerfelix.data.remote.model

import com.google.gson.annotations.SerializedName

data class PlacesApiResponse(
    @SerializedName("results")
    val results: List<PlaceResult>,

    @SerializedName("status")
    val status: String
)

data class PlaceResult(
    @SerializedName("name")
    val name: String? = null,

    @SerializedName("vicinity")
    val vicinity: String? = null,

    @SerializedName("geometry")
    val geometry: Geometry? = null,

    @SerializedName("rating")
    val rating: Double? = null,

    @SerializedName("user_ratings_total")
    val userRatingsTotal: Int? = null,

    @SerializedName("place_id")
    val placeId: String? = null
)