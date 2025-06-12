package com.example.travellerfelix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travellerfelix.data.repository.NearbyRepository

class NearbyViewModelFactory(private val repository: NearbyRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NearbyViewModel::class.java)) {
            return NearbyViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}