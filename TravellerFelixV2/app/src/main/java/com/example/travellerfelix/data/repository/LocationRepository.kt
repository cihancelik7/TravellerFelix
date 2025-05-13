package com.example.travellerfelix.data.repository

import android.util.Log
import com.example.travellerfelix.data.remote.GeoNamesApiService

class LocationRepository(private val apiService: GeoNamesApiService) {

    suspend fun fetchCountries(username: String): List<Pair<String, String>> {
        return try {
            Log.d("DEBUG", "GeoNames API çağrısı başlatılıyor...")

            val response = apiService.getCountries(username)
            if (response.geonames.isNotEmpty()) {
                response.geonames.map { it.countryName to it.countryCode }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("ERROR", "GeoNames API çağrısında hata oluştu: ${e.message}")
            emptyList()
        }
    }

    suspend fun fetchCitiesByCountry(countryCode: String, username: String): List<String> {
        return try {
            Log.d("DEBUG", "Şehir verisi çekiliyor... Ülke kodu: $countryCode")

            val response = apiService.getCitiesByCountry(countryCode, username = username)
            if (response.cities.isNotEmpty()) {
                response.cities.map { it.cityName }
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            Log.e("ERROR", "Şehir API çağrısında hata oluştu: ${e.message}")
            emptyList()
        }
    }
}