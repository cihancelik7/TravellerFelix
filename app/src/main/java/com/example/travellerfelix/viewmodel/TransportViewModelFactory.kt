package com.example.travellerfelix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travellerfelix.data.repository.TravelRepository

class TransportViewModelFactory(private val repository: TravelRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TransportViewModel::class.java)) {
            return TransportViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}