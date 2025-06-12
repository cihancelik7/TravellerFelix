package com.example.travellerfelix.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.travellerfelix.data.local.model.City
import kotlinx.coroutines.flow.Flow

@Dao
interface CityDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCity(city: City): Long

    @Query("SELECT * FROM city WHERE countryId = :countryId ORDER BY name ASC")
    fun getCitiesByCountry(countryId: Int): Flow<List<City>>

    @Query("UPDATE city SET date = :date WHERE id = :cityId")
    suspend fun updateCityDate(cityId: Int, date: String)

    @Query("DELETE FROM city")
    suspend fun deleteAll()
}