package com.example.travellerfelix.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.travellerfelix.data.local.model.DayPlan
import kotlinx.coroutines.flow.Flow

@Dao
interface DayPlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDayPlan(dayPlan: DayPlan): Long  // ✅ Long döndürüyor!

    @Delete
    suspend fun deleteDayPlan(dayPlan: DayPlan): Int  // ✅ Int döndürüyor!

    @Query("SELECT * FROM day_plan WHERE cityId = :cityId ORDER BY date ASC")
    fun getDayPlansByCity(cityId: Int): Flow<List<DayPlan>>

    @Query("SELECT * FROM day_plan WHERE date = :date")
    suspend fun getDayPlansByDate(date: String): List<DayPlan>

    @Query("SELECT * FROM day_plan WHERE date BETWEEN :startDate AND :endDate")
    suspend fun getDayPlansInRange(startDate: String, endDate: String): List<DayPlan>

    // 📌 **Sadece belirli bir tarihe ait ID'yi döndür**
    @Query("SELECT id FROM day_plan WHERE date = :date LIMIT 1")
    suspend fun getDayPlanIdByDate(date: String): Int?

    @Query("SELECT * FROM day_plan")
    fun getAllDayPlans(): List<DayPlan>

    @Query("DELETE FROM day_plan")
    suspend fun deleteAll()


}