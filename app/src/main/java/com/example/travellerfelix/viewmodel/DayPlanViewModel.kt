package com.example.travellerfelix.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travellerfelix.data.local.model.AllDayPlans
import com.example.travellerfelix.data.repository.TravelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DayPlanViewModel(private val repository: TravelRepository) : ViewModel() {

    private val _dayPlans = MutableStateFlow<AllDayPlans?>(null)
    val dayPlans: StateFlow<AllDayPlans?> get() = _dayPlans

    fun loadDayPlans(dayPlanId: Int) {
        viewModelScope.launch {
            repository.getAllPlansByDayPlan(dayPlanId).collect { allPlans ->
                _dayPlans.value = allPlans
                Log.d("DayPlanViewModel", "DayPlan $dayPlanId için veriler alındı: $allPlans")
            }
        }
    }
    fun deleteAllData(){
        viewModelScope.launch {
            repository.deleteAllData()
        }
    }
}

class DayPlanViewModelFactory(
    private val repository: TravelRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DayPlanViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DayPlanViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}