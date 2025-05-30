package com.example.travellerfelix.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.travellerfelix.data.local.model.TomorrowPlanItem
import com.example.travellerfelix.data.repository.TomorrowPlanRepository

class TomorrowPlanViewModel(
    private val repository : TomorrowPlanRepository
): ViewModel() {

    private val _plans = MutableLiveData<List<TomorrowPlanItem>>()
    val plans: LiveData<List<TomorrowPlanItem>> get() = _plans

    suspend fun fetchPlans(){
        _plans.value = repository.getTomorrowPlans()
    }

}