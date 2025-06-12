package com.example.travellerfelix.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "rent")
data class Rent(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dayPlanId: Int,
    val rentalCompany: String,
    val pickupDate: String,
    val dropOffDate: String,
    val pickUpTime: String,
    val dropOffTime: String,
    val pickupLocation: String,
    val dropOffLocation: String,
    val carModel: String,
    override val city: String,
    override val country: String
):CityCountryProvider
