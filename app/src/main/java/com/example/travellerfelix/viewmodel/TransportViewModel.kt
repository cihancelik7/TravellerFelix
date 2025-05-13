package com.example.travellerfelix.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.travellerfelix.data.local.model.Transport
import com.example.travellerfelix.data.repository.TravelRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TransportViewModel(private val repository: TravelRepository) : ViewModel() {

    private val _transports = MutableStateFlow<List<Transport>>(emptyList())
    val transports: StateFlow<List<Transport>> get() = _transports

    fun insertTransport(transport: Transport) {
        viewModelScope.launch {
            repository.insertTransport(transport)
            loadTransports(transport.dayPlanId)
        }
    }

    fun deleteTransport(transport: Transport) {
        viewModelScope.launch {
            repository.deleteTransport(transport)
            loadTransports(transport.dayPlanId)
        }
    }

    private fun loadTransports(dayPlanId: Int) {
        viewModelScope.launch {
            repository.getTransportsByDayPlan(dayPlanId).collect { transportList ->
                _transports.value = transportList
            }
        }
    }
}