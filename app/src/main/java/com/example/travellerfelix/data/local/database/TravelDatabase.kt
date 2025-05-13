package com.example.travellerfelix.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.travellerfelix.data.local.dao.*
import com.example.travellerfelix.data.local.model.*

@Database(
    entities = [City::class, DayPlan::class,Country::class, Rent::class,Museum::class, Transport::class, Reminder::class,Restaurant::class, Hotel::class, Note::class,Place::class], // 📌 ✅ Hotel modelini ekledik!
    version = 1,
    exportSchema = false
)
abstract class TravelDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao
    abstract fun dayPlanDao(): DayPlanDao
    abstract fun transportDao(): TransportDao
    abstract fun reminderDao(): ReminderDao
    abstract fun hotelDao(): HotelDao  // 📌 ✅ Buraya HotelDao’yu ekledik!
    abstract fun rentDao(): RentDao
    abstract fun museumDao(): MuseumDao
    abstract fun restaurantDao(): RestaurantDao
    abstract fun countryDao(): CountryDao
    abstract fun noteDao(): NoteDao
    abstract fun placeDao(): PlaceDao



    companion object {
        @Volatile
        private var INSTANCE: TravelDatabase? = null

        fun getDatabase(context: Context): TravelDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TravelDatabase::class.java,
                    "travel_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}