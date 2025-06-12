package com.example.travellerfelix.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Country")
data class Country(
    @PrimaryKey(autoGenerate = true)
    val id : Int = 0,
    val name: String,
    val date: String? = null
)
