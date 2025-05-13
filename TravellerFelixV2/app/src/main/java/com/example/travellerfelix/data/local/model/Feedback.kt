package com.example.travellerfelix.data.local.model

data class Feedback(
    val message: String = "",
    val city: String = "",
    val country: String = "",
    val timestamp: Long = System.currentTimeMillis()
)
