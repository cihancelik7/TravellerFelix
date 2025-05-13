package com.example.travellerfelix.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transport")
data class Transport(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dayPlanId: Int,
    val transportType: String, // Bus, Train, Flight, etc.
    val ticketName: String, // Nereden
    val ticketDate: String, // Nereye
    val reservationTime: String,
    val departureTime: String,
    val otherTransportType: String? = null, override val city: String, override val country: String
):CityCountryProvider