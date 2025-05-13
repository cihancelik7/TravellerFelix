package com.example.travellerfelix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travellerfelix.data.local.model.Museum
import com.example.travellerfelix.data.repository.TravelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MuseumViewModel(private val repository: TravelRepository): ViewModel() {
    private val _museums = MutableStateFlow<List<Museum>>(emptyList())
    val museums : StateFlow<List<Museum>> get() = _museums

    fun insertMuseum(museum:Museum){
        viewModelScope.launch {
            repository.insertMuseum(museum)
            loadMuseums(museum.dayPlanId)
        }
    }
    fun deleteMuseum(museum: Museum){
        viewModelScope.launch {
            repository.deleteMuseum(museum)
            loadMuseums(museum.dayPlanId)
        }
    }



    fun loadMuseums(dayPlanId:Int){
        viewModelScope.launch {
            repository.getMuseumsByDayPlan(dayPlanId).collect{museumList ->
                _museums.value = museumList
            }
        }
    }
}