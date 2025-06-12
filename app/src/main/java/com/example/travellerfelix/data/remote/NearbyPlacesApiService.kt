package com.example.travellerfelix.data.remote

import com.example.travellerfelix.data.remote.model.PlacesApiResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NearbyPlacesApiService {
    @GET("maps/api/place/nearbysearch/json")
    suspend fun getNearbyPlaces(
        @Query("location") location: String, // "latitude,longitude"
        @Query("radius") radius: Int,        // örneğin 1000 (metre)
        @Query("type") type: String,         // örneğin "restaurant"
        @Query("key") key: String            // API anahtarın
    ): Response<PlacesApiResponse>
}