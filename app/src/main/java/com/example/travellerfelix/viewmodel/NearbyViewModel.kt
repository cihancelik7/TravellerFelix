package com.example.travellerfelix.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travellerfelix.data.remote.model.Place
import com.example.travellerfelix.data.remote.model.PlaceResult
import com.example.travellerfelix.data.remote.model.PlacesApiResponse
import com.example.travellerfelix.data.repository.NearbyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NearbyViewModel(private val repository: NearbyRepository) : ViewModel() {

    private val _nearbyPlaces = MutableStateFlow<List<PlacesApiResponse>>(emptyList())
    val nearbyPlaces: StateFlow<List<PlacesApiResponse>> = _nearbyPlaces

    private val _visiblePlaces = MutableStateFlow<List<PlaceResult>>(emptyList()) 
    val visiblePlaces: StateFlow<List<PlaceResult>> = _visiblePlaces

    private var allResults: List<PlaceResult> = emptyList() // ✅ DÜZENLENDİ
    private var currentIndex = 0
    private val pageSize = 10

    fun fetchNearbyPlaces(location: String, radius: Int, types: List<String>) {
        viewModelScope.launch {
            // Listeyi sıfırla (yeni kategoriye geçildiğinde önceki veriler temizlensin)
            _visiblePlaces.value = emptyList()
            allResults = emptyList()
            currentIndex = 0

            val responses = repository.getNearbyPlacesForTypes(location, radius, types)
            _nearbyPlaces.value = responses
            allResults = responses.flatMap { it.results }
            loadNextPage()
        }
    }

    fun loadNextPage() {
        if (currentIndex >= allResults.size) return
        val nextPage = allResults.drop(currentIndex).take(pageSize)
        _visiblePlaces.value = _visiblePlaces.value + nextPage
        currentIndex += pageSize
    }

    fun totalCount(): Int = allResults.size
}