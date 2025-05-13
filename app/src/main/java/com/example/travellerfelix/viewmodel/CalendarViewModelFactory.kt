package com.example.travellerfelix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.travellerfelix.data.local.dao.DayPlanDao

class CalendarViewModelFactory(private val dayPlanDao: DayPlanDao): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)){
            return CalendarViewModel(dayPlanDao) as T
        }
        throw IllegalArgumentException("Unknown viewmodel Class")
    }
}