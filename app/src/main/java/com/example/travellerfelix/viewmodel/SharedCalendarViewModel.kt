package com.example.travellerfelix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class SharedCalendarViewModel : ViewModel() {
    val refreshSignal = MutableStateFlow(false)

    fun sendRefreshSignal() {
        viewModelScope.launch {
            refreshSignal.emit(true)
        }
    }
    fun triggerDotRefresh() {
        refreshSignal.value = true
        refreshSignal.value = false // yeniden tetiklenebilmesi için hemen sıfırla
    }

}