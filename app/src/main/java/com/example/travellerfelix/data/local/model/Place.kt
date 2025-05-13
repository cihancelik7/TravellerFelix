package com.example.travellerfelix.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "place")
data class Place(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dayPlanId: Int,
    val name: String,
    val type: String,
    val openTime: String,
    val closeTime: String,
    val latitude: Double,
    val longitude: Double,
    override val city: String,
    override val country: String
):CityCountryProvider

