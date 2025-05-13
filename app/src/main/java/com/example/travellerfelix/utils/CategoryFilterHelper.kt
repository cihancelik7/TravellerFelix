package com.example.travellerfelix.utils

import com.example.travellerfelix.data.local.model.AllDayPlans
import com.example.travellerfelix.data.repository.TravelRepository
import kotlinx.coroutines.flow.firstOrNull

object CategoryFilterHelper {

    suspend fun filterByCategory(
        selectedDate: String,
        selectedCategory: String,
        selectedCity: String,
        selectedCountry: String,
        repository: TravelRepository,
    ): AllDayPlans {
        return when (selectedCategory) {
            "Hotel" -> {
                val hotels = repository.getHotelsByDate(selectedDate).firstOrNull()
                    ?.filter { it.city == selectedCity && it.country == selectedCountry }
                    ?: emptyList()
                AllDayPlans(hotels = hotels)
            }

            "Rent a Car" -> {
                val rents = repository.getRentsByDate(selectedDate).firstOrNull()
                    ?.filter { it.city == selectedCity && it.country == selectedCountry }
                    ?: emptyList()
                AllDayPlans(rents = rents)
            }

            "Museum" -> {
                val museums = repository.getMuseumsByDate(selectedDate).firstOrNull()
                    ?.filter { it.city == selectedCity && it.country == selectedCountry }
                    ?: emptyList()
                AllDayPlans(museums = museums)
            }

            "Transport" -> {
                val transports = repository.getTransportsByDate(selectedDate).firstOrNull()
                    ?.filter { it.city == selectedCity && it.country == selectedCountry }
                    ?: emptyList()
                AllDayPlans(transports = transports)
            }

            "Restaurant" -> {
                val restaurants = repository.getRestaurantsByDate(selectedDate).firstOrNull()
                    ?.filter { it.city == selectedCity && it.country == selectedCountry }
                    ?: emptyList()
                AllDayPlans(restaurants = restaurants)
            }

            "All" -> {
                val hotels = repository.getHotelsByDate(selectedDate).firstOrNull()
                    ?.filter { it.city == selectedCity && it.country == selectedCountry }
                    ?: emptyList()

                val rents = repository.getRentsByDate(selectedDate).firstOrNull()
                    ?.filter { it.city == selectedCity && it.country == selectedCountry }
                    ?: emptyList()

                val museums = repository.getMuseumsByDate(selectedDate).firstOrNull()
                    ?.filter { it.city == selectedCity && it.country == selectedCountry }
                    ?: emptyList()

                val transports = repository.getTransportsByDate(selectedDate).firstOrNull()
                    ?.filter { it.city == selectedCity && it.country == selectedCountry }
                    ?: emptyList()

                val restaurants = repository.getRestaurantsByDate(selectedDate).firstOrNull()
                    ?.filter { it.city == selectedCity && it.country == selectedCountry }
                    ?: emptyList()

                AllDayPlans(
                    hotels = hotels,
                    rents = rents,
                    museums = museums,
                    transports = transports,
                    restaurants = restaurants
                )
            }

            else -> AllDayPlans()
        }
    }
}