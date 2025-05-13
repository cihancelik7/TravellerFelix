package com.example.travellerfelix.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travellerfelix.data.remote.model.PlacesApiResponse
import com.example.travellerfelix.data.repository.NearbyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NearbyViewModel(private val repository: NearbyRepository) : ViewModel() {

    private val _nearbyPlaces = MutableStateFlow<List<PlacesApiResponse>>(emptyList())
    val nearbyPlaces: StateFlow<List<PlacesApiResponse>> = _nearbyPlaces

    fun fetchNearbyPlaces(location: String, radius: Int, types: List<String>) {
        Log.d("NearbyViewModel", "🛰️ Veri çekiliyor - Lokasyon: $location, Tipler: $types")

        viewModelScope.launch {
            val responses = repository.getNearbyPlacesForTypes(location, radius, types)

            Log.d("NearbyViewModel", "📥 API'den dönen response listesi: ${responses.size}")
            responses.forEachIndexed { index, response ->
                Log.d("NearbyViewModel", "📦 [$index] Status: ${response.status}, Result Count: ${response.results.size}")
                response.results.forEach {
                    Log.d("NearbyViewModel", "➡️ ${it.name} | ${it.vicinity} | ⭐ ${it.rating}")
                }
            }

            _nearbyPlaces.value = responses
        }
    }
}