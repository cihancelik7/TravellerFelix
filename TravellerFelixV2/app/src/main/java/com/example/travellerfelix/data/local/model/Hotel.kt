package com.example.travellerfelix.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "hotel")
data class Hotel(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dayPlanId: Int,  // 📌 Bağlı olduğu günün ID’si
    val name: String, // 📌 Otel adı
    val checkInDate: String, // 📌 Giriş tarihi
    val checkOutDate: String, // 📌 Çıkış tarihi
    val checkInTime: String, // 📌 Giriş saati
    val checkOutTime: String, // 📌 Çıkış saati
    override val city: String,
    override val country: String,
):CityCountryProvider