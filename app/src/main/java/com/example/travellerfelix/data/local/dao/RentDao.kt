package com.example.travellerfelix.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.travellerfelix.data.local.model.Rent
import kotlinx.coroutines.flow.Flow

@Dao
interface RentDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRent(rent: Rent): Long

    @Delete
    suspend fun deleteRent(rent: Rent): Int

    @Query("SELECT * FROM rent WHERE dayPlanId = :dayPlanId ORDER BY pickupDate ASC")
    fun getRentsByDayPlan(dayPlanId: Int) : Flow<List<Rent>>

    // 📌 **Tarih bazlı kiralık araçları getir**
    @Query("""
    SELECT * FROM rent 
    INNER JOIN day_plan ON rent.dayPlanId = day_plan.id 
    WHERE :date BETWEEN rent.pickupDate AND rent.dropOffDate
""")
    fun getRentsByDate(date: String): Flow<List<Rent>>

    @Query("DELETE FROM rent")
    suspend fun deleteAll()

    @Query("SELECT * FROM rent")
    suspend fun getAllRents(): List<Rent>

    @Query("""
        SELECT * FROM rent
        INNER JOIN day_plan ON rent.dayPlanId = day_plan.id
        WHERE :date BETWEEN rent.pickupDate AND rent.dropOffDate
        """)
    suspend fun getRentsForDate(date:String) :List<Rent>

}