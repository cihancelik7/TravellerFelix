    package com.example.travellerfelix.data.local.model

    import androidx.room.Entity
    import androidx.room.PrimaryKey

    @Entity(tableName = "museum")
    data class Museum (
        @PrimaryKey(autoGenerate = true)
        val id: Int = 0,
        val dayPlanId: Int,
        val name: String,
        val visitDate: String,
        val visitTime: String,
        val openCloseTime: String,
        override val city: String,
        override val country: String
    ):CityCountryProvider