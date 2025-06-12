package com.example.travellerfelix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travellerfelix.data.repository.LocationRepository

class LocationViewModelFactory(private val repository: LocationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(LocationViewModel::class.java)){
            return LocationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown viewmodel class")
    }
}