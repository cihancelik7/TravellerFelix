package com.example.travellerfelix.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travellerfelix.data.repository.LocationRepository
import kotlinx.coroutines.launch

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {

    private val _countries = MutableLiveData<List<Pair<String, String>>>()
    val countries: LiveData<List<Pair<String, String>>> get() = _countries

    private val _cities = MutableLiveData<List<String>>()
    val cities: LiveData<List<String>> get() = _cities

    fun fetchCountries(username: String) {
        viewModelScope.launch {
            val countryList = repository.fetchCountries(username)
            _countries.postValue(countryList)
        }
    }

    fun fetchCitiesByCountry(countryCode: String, username: String) {
        viewModelScope.launch {
            val cityList = repository.fetchCitiesByCountry(countryCode, username)
            _cities.postValue(cityList)
        }
    }
}