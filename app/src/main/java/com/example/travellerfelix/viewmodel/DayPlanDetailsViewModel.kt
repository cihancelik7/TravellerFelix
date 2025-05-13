package com.example.travellerfelix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travellerfelix.data.repository.TravelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DayPlanDetailsViewModel(private val repository: TravelRepository): ViewModel() {


    private val _dayPlanDetails = MutableStateFlow<List<Any>>(emptyList())
    val dayPlanDetails: StateFlow<List<Any>> get() = _dayPlanDetails

    fun loadDayPlanDetails(dayPlanId: Int){
        viewModelScope.launch {
            val allDetails = mutableListOf<Any>()

            repository.getHotelsByDayPlan(dayPlanId).collect{allDetails.addAll(it)}
            repository.getRestaurantsByDayPlan(dayPlanId).collect{allDetails.addAll(it)}
            repository.getTransportsByDayPlan(dayPlanId).collect{allDetails.addAll(it)}
            repository.getRentsByDayPlan(dayPlanId).collect{allDetails.addAll(it)}
            repository.getMuseumsByDayPlan(dayPlanId).collect{allDetails.addAll(it)}

            _dayPlanDetails.value = allDetails
        }
    }
}