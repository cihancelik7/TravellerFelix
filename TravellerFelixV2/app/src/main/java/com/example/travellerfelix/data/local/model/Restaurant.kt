package com.example.travellerfelix.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "restaurant")
data class Restaurant (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dayPlanId: Int,
    val name: String,
    val reservationDate: String,
    val reservationTime: String,
    val openCloseTime: String,
    override val city: String,
    override val country: String

):CityCountryProvider