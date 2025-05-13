package com.example.travellerfelix.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.travellerfelix.data.local.model.Reminder
import com.example.travellerfelix.data.repository.TravelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReminderViewModel(application: Application,private val repository: TravelRepository):AndroidViewModel(application) {
    private val _reminders = MutableStateFlow<List<Reminder>>(emptyList())
    val reminders: StateFlow<List<Reminder>> get() = _reminders

    fun insertReminder(reminder:Reminder){
        viewModelScope.launch {
            repository.insertReminder(reminder)
            loadReminders(reminder.date)
        }
    }

    fun deleteReminder(reminder:Reminder){
        viewModelScope.launch {
            repository.deleteReminder(reminder)
            loadReminders(reminder.date)

        }
    }

    private fun loadReminders(date:String){
        viewModelScope.launch {
            repository.getRemindersByDate(date).collect{remindList ->
                _reminders.value = remindList
            }
        }
    }


}