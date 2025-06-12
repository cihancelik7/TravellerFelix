package com.example.travellerfelix.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.travellerfelix.data.local.model.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: Reminder): Long

    @Delete
    suspend fun deleteReminder(reminder: Reminder): Int

    @Query("SELECT * FROM reminder WHERE date = :date ORDER BY time ASC")
    fun getRemindersByDate(date: String): Flow<List<Reminder>>
}