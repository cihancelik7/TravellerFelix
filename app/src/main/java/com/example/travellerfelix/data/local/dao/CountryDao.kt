package com.example.travellerfelix.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.travellerfelix.data.local.model.Country
import kotlinx.coroutines.flow.Flow

@Dao
interface CountryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCountry(country: Country): Long

    @Query("SELECT * FROM country ORDER BY name ASC")
    fun getAllCountries(): Flow<List<Country>>

    @Query("UPDATE country SET date = :date WHERE id = :countryId")
    suspend fun updateCountryDate(countryId: Int, date: String)

    @Query("DELETE FROM country")
    suspend fun deleteAll()

}