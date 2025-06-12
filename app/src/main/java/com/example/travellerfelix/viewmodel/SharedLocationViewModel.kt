package com.example.travellerfelix.viewmodel

import android.location.Geocoder
import androidx.lifecycle.ViewModel
import com.example.travellerfelix.data.local.model.DeviceLocation
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class SharedLocationViewModel : ViewModel() {

    private val _currentLocation = MutableStateFlow<DeviceLocation?>(null)
    val currentLocation: StateFlow<DeviceLocation?> = _currentLocation

    private val _city = MutableStateFlow("Bilinmeyen Şehir")
    val city: StateFlow<String> = _city

    private val _country = MutableStateFlow("Bilinmeyen Ülke")
    val country: StateFlow<String> = _country

    fun updateLocation(location: DeviceLocation, geocoder: Geocoder) {
        _currentLocation.value = location

        try {
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            if (!addresses.isNullOrEmpty()) {
                val address = addresses[0]
                _city.value = address.locality ?: address.subAdminArea ?: address.adminArea ?: "Bilinmeyen Şehir"
                _country.value = address.countryName ?: "Bilinmeyen Ülke"
            }
        } catch (e: Exception) {
            // Loglamak istersen buraya koyabilirsin
        }
    }
}