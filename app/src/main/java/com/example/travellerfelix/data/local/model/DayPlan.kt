package com.example.travellerfelix.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "day_plan")
data class DayPlan(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cityId: Int,
    val date: String
)