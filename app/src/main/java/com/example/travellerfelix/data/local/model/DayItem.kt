package com.example.travellerfelix.data.local.model

data class DayItem(
    val date: String,
    val dayName: String,
    val isAvailable: Boolean
) {
    override fun toString(): String {
        return "DayItem(date=$date, isAvailable=$isAvailable)"
    }
}
