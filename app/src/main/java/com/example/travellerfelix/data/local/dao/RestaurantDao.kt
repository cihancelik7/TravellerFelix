    package com.example.travellerfelix.data.local.dao

    import androidx.room.Dao
    import androidx.room.Delete
    import androidx.room.Insert
    import androidx.room.OnConflictStrategy
    import androidx.room.Query
    import com.example.travellerfelix.data.local.model.Restaurant
    import kotlinx.coroutines.flow.Flow

    @Dao
    interface RestaurantDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertRestaurant(restaurant:Restaurant): Long

        @Delete
        suspend fun deleteRestaurant(restaurant: Restaurant): Int

        @Query("SELECT * FROM restaurant WHERE dayPlanId = :dayPlanId ORDER BY reservationTime ASC")
        fun getRestaurantsByDayPlan(dayPlanId: Int): Flow<List<Restaurant>>

        // 📌 **Tarih bazlı restoranları getir**
        @Query("SELECT * FROM restaurant WHERE reservationDate = :date")
        fun getRestaurantsByDate(date: String): Flow<List<Restaurant>>

        @Query("DELETE FROM restaurant")
        suspend fun deleteAll()

        @Query("SELECT * FROM restaurant WHERE reservationDate = :date")
        suspend fun getRestaurantsForDate(date: String): List<Restaurant>

    }