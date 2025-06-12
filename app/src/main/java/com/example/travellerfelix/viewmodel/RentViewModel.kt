package com.example.travellerfelix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travellerfelix.data.local.model.Rent
import com.example.travellerfelix.data.repository.TravelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RentViewModel(private val repository: TravelRepository) : ViewModel(){

    private val _rents = MutableStateFlow<List<Rent>>(emptyList())
    val rents: StateFlow<List<Rent>> get() = _rents

    fun insertRent (rent: Rent){
        viewModelScope.launch {
            repository.insertRent(rent)
            loadRents(rent.dayPlanId)
        }
    }
    fun deleteRent(rent:Rent){
        viewModelScope.launch {
            repository.deleteRent(rent)
            loadRents(rent.dayPlanId)
        }
    }





    fun loadRents(dayPlanId: Int){

        viewModelScope.launch {
            repository.getRentsByDayPlan(dayPlanId).collect{rentList->
                _rents.value = rentList
            }
        }

    }


}