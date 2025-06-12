package com.example.travellerfelix.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reminder")
data class Reminder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val dayPlanId: Int, // Hangi günle bağlantılı olduğunu tutar
    val title: String,
    val date: String,
    val time: String
)