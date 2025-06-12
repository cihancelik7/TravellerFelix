package com.example.travellerfelix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travellerfelix.data.local.model.Hotel
import com.example.travellerfelix.data.repository.TravelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HotelViewModel(private val repository: TravelRepository) : ViewModel() {

    private val _hotels = MutableStateFlow<List<Hotel>>(emptyList())
    val hotels: StateFlow<List<Hotel>> get() = _hotels

    fun insertHotel(hotel: Hotel) {
        viewModelScope.launch {
            repository.insertHotel(hotel)
            loadHotels(hotel.dayPlanId)
        }
    }

    fun deleteHotel(hotel: Hotel) {
        viewModelScope.launch {
            repository.deleteHotel(hotel)
            loadHotels(hotel.dayPlanId)
        }
    }

    private fun loadHotels(dayPlanId: Int) {
        viewModelScope.launch {
            repository.getHotelsByDayPlan(dayPlanId).collect { hotelList ->
                _hotels.value = hotelList
            }
        }
    }
}