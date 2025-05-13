package com.example.travellerfelix.data.local.model

data class AllDayPlans(
    val hotels: List<Hotel> = emptyList(),
    val restaurants: List<Restaurant> = emptyList(),
    val transports: List<Transport> = emptyList(),
    val rents: List<Rent> = emptyList(),
    val museums: List<Museum> = emptyList()
)