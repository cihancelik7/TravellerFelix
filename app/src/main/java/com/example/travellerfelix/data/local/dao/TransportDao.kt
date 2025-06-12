package com.example.travellerfelix.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.travellerfelix.data.local.model.Transport
import kotlinx.coroutines.flow.Flow

@Dao
interface TransportDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTransport(transport: Transport): Long

    @Delete
    suspend fun deleteTransport(transport: Transport): Int

    @Query("SELECT * FROM transport WHERE dayPlanId = :dayPlanId ORDER BY ticketDate ASC")
    fun getTransportByDayPlan(dayPlanId: Int): Flow<List<Transport>>

    // 📌 **Tarih bazlı ulaşımları getir**
    @Query("SELECT * FROM transport WHERE ticketDate = :date")
    fun getTransportsByDate(date: String): Flow<List<Transport>>

    @Query("DELETE FROM transport")
    suspend fun deleteAll()

    @Query("SELECT * FROM transport WHERE ticketDate = :date")
    suspend fun getTransportForDate(date:String) : List<Transport>
}