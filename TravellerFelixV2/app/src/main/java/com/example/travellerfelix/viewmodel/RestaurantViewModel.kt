package com.example.travellerfelix.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travellerfelix.data.local.model.Restaurant
import com.example.travellerfelix.data.repository.TravelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RestaurantViewModel(private val repository: TravelRepository): ViewModel() {
    private val _restaurants = MutableStateFlow<List<Restaurant>> (emptyList())
    val restaurants : StateFlow<List<Restaurant>> get() = _restaurants

    fun insertRestaurant(restaurant: Restaurant) {
        viewModelScope.launch {
            Log.d("RestaurantInsertTest", "insertRestaurant çağrıldı: $restaurant")

            val id = repository.insertRestaurant(restaurant)
            Log.d("RestaurantInsertTest", "Veritabanına eklendi. Yeni ID: $id")

            loadRestaurants(restaurant.dayPlanId)
        }
    }
    fun deleteRestaurant(restaurant: Restaurant){
        viewModelScope.launch {
            repository.deleteRestaurant(restaurant)
            loadRestaurants(restaurant.dayPlanId)
        }
    }


    private fun loadRestaurants(dayPlanId: Int){
        viewModelScope.launch {
            repository.getRestaurantsByDayPlan(dayPlanId).collect{restaurantList ->
                _restaurants.value = restaurantList
            }
        }
    }


}