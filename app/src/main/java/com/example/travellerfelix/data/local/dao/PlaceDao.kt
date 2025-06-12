    package com.example.travellerfelix.data.local.dao

    import androidx.room.Dao
    import androidx.room.Delete
    import androidx.room.Insert
    import androidx.room.OnConflictStrategy
    import androidx.room.Query
    import com.example.travellerfelix.data.local.model.DayPlan
    import com.example.travellerfelix.data.local.model.Place
    import kotlinx.coroutines.flow.Flow

    @Dao
    interface PlaceDao {
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insertPlace(place: Place): Long

        @Delete
        suspend fun deletePlace(place: Place): Int

        @Query("SELECT * FROM place WHERE dayPlanId = :dayPlanId ORDER BY name ASC")
        fun getPlacesByDayPlan(dayPlanId: Int): Flow<List<Place>>

        @Query("DELETE FROM place")
        suspend fun deleteAll()
    }