package com.example.travellerfelix.data.repository

import android.util.Log
import com.example.travellerfelix.BuildConfig
import com.example.travellerfelix.data.remote.NearbyPlacesApiService
import com.example.travellerfelix.data.remote.model.PlacesApiResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response

class NearbyRepository(private val apiService: NearbyPlacesApiService) {

    suspend fun getNearbyPlacesForTypes(
        location: String,
        radius: Int,
        types: List<String>
    ): List<PlacesApiResponse> = withContext(Dispatchers.IO) {   // ✅ Değişiklik burada
        val results = mutableListOf<PlacesApiResponse>()

        for (type in types) {
            Log.d("NearbyRepository", "🛰️ API çağrısı yapılıyor - Type: $type")

            val response: Response<PlacesApiResponse> = apiService.getNearbyPlaces(
                location = location,
                radius = radius,
                type = type,
                key = BuildConfig.GOOGLE_MAPS_API_KEY
            )

            if (response.isSuccessful) {
                Log.d("NearbyRepository", "✅ Başarılı response, Tip: $type")
                response.body()?.let {
                    results.add(it)
                }
            } else {
                Log.d("NearbyRepository", "❌ Hata oluştu, Tip: $type, Status: ${response.code()}")
            }
        }

        results
    }
}