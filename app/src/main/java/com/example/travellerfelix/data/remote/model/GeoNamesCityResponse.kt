package com.example.travellerfelix.data.remote.model

import com.google.gson.annotations.SerializedName

data class GeoNamesCityResponse(
    @SerializedName("geonames") val cities: List<GeoNamesCity>
)

data class GeoNamesCity(
    @SerializedName("name") val cityName: String
)