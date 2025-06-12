package com.example.travellerfelix.data.remote

import com.google.gson.annotations.SerializedName

data class GeoNamesResponse(
    @SerializedName("geonames") val geonames: List<GeoNamesCountry>
)

data class GeoNamesCountry(
    @SerializedName("countryName") val countryName: String,
    @SerializedName("countryCode") val countryCode: String
)