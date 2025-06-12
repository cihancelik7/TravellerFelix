package com.example.travellerfelix.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.travellerfelix.data.local.model.Hotel
import kotlinx.coroutines.flow.Flow

@Dao
interface HotelDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertHotel(hotel: Hotel): Long

    @Delete
    suspend fun deleteHotel(hotel: Hotel): Int

    @Query("SELECT * FROM hotel WHERE dayPlanId = :dayPlanId ORDER BY checkInDate ASC")
    fun getHotelsByDayPlan(dayPlanId: Int): Flow<List<Hotel>>

    @Query("""
        SELECT * FROM hotel 
        INNER JOIN day_plan ON hotel.dayPlanId = day_plan.id 
        WHERE :date BETWEEN hotel.checkInDate AND hotel.checkOutDate
    """)
    fun getHotelsByDate(date: String): Flow<List<Hotel>>

    @Query("SELECT * FROM hotel")
    suspend fun getAllHotels(): List<Hotel> // 📌 EKLENDİ: Tüm otelleri almak için

    @Query("DELETE FROM hotel")
    suspend fun deleteAll()

    @Query("""
        SELECT * FROM hotel
        INNER JOIN day_plan ON hotel.dayPlanId = day_plan.id
        WHERE :date BETWEEN hotel.checkInDate AND hotel.checkOutDate
        """)
    suspend fun getHotelsForDate(date: String): List<Hotel>
}