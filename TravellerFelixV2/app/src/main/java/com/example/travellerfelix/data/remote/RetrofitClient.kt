package com.example.travellerfelix.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    // 🌍 GeoNames API
    val geoNamesApiService: GeoNamesApiService by lazy {
        Retrofit.Builder()
            .baseUrl("http://api.geonames.org/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GeoNamesApiService::class.java)
    }

    // 📍 Google Places API
    val nearbyPlacesApiService: NearbyPlacesApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/") // ⚠️ dikkat: HTTPS kullanıyoruz
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(NearbyPlacesApiService::class.java)
    }
}