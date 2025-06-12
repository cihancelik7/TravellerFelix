package com.example.travellerfelix.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.travellerfelix.data.local.model.Museum
import kotlinx.coroutines.flow.Flow

@Dao
interface MuseumDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMuseum(museum:Museum): Long

    @Delete
    suspend fun deleteMuseum(museum: Museum): Int

    @Query("SELECT * FROM museum WHERE dayPlanId = :dayPlanId ORDER BY visitDate ASC")
    fun getMuseumsByDayPlan(dayPlanId: Int): Flow<List<Museum>>

    // 📌 **Tarih bazlı müzeleri getir**
    @Query("SELECT * FROM museum WHERE visitDate = :date")
    fun getMuseumsByDate(date: String): Flow<List<Museum>>

    @Query("DELETE FROM museum")
    suspend fun deleteAll()


    @Query("SELECT * FROM museum WHERE visitDate = :date")
    suspend fun getMuseumForDate(date:String) : List<Museum>
}