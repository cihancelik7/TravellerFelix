package com.example.travellerfelix.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "city")
data class City(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val countryId: Int,  // 📌 Bağlı olduğu ülkenin ID'si
    val name: String, // 📌 Şehir adı
    val date: String? = null
)