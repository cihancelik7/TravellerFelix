package com.example.travellerfelix.data.remote

import com.example.travellerfelix.data.remote.model.GeoNamesCityResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface GeoNamesApiService {

    @GET("countryInfoJSON")
    suspend fun getCountries(
        @Query("username") username: String
    ): GeoNamesResponse

    @GET("searchJSON")
    suspend fun getCitiesByCountry(
        @Query("country") countryCode: String,
        @Query("featureClass") featureClass: String = "P",
        @Query("maxRows") maxRows: Int = 1000,
        @Query("username") username: String
    ): GeoNamesCityResponse
}