package com.example.travellerfelix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travellerfelix.data.repository.TomorrowPlanRepository

class TomorrowPlanViewModelFactory(
    private val repository: TomorrowPlanRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TomorrowPlanViewModel(repository) as T
    }
}