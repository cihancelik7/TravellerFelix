package com.example.travellerfelix.data.repository

import com.example.travellerfelix.data.local.dao.*
import com.example.travellerfelix.data.local.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class TravelRepository(
    private val cityDao: CityDao,
    private val dayPlanDao: DayPlanDao,
    private val hotelDao: HotelDao,
    private val transportDao: TransportDao,
    private val reminderDao: ReminderDao,
    private val rentDao: RentDao,
    private val museumDao: MuseumDao,
    private val restaurantDao: RestaurantDao,
    private val noteDao: NoteDao,
    private val placeDao: PlaceDao,
    private val countryDao: CountryDao
) {


    // 📌 **Günlük Plan İşlemleri**
    suspend fun insertDayPlan(dayPlan: DayPlan) = dayPlanDao.insertDayPlan(dayPlan)
    suspend fun deleteDayPlan(dayPlan: DayPlan) = dayPlanDao.deleteDayPlan(dayPlan)
    fun getDayPlansByCity(cityId: Int): Flow<List<DayPlan>> = dayPlanDao.getDayPlansByCity(cityId)

    // 📌 **Seçilen tarihe ait DayPlan ID'sini getir**
    suspend fun getDayPlanIdByDate(date: String): Int? = dayPlanDao.getDayPlanIdByDate(date)

    // 📌 **Seçilen tarihe göre `DayPlan` kontrol et**
    suspend fun getDayPlanByDate(date: String): List<DayPlan> = dayPlanDao.getDayPlansByDate(date)

    // 📌 **Gezilecek Yer İşlemleri**
    suspend fun insertHotel(hotel: Hotel) = hotelDao.insertHotel(hotel)
    suspend fun deleteHotel(hotel: Hotel) = hotelDao.deleteHotel(hotel)
    fun getHotelsByDayPlan(dayPlanId: Int): Flow<List<Hotel>> = hotelDao.getHotelsByDayPlan(dayPlanId)
    fun getHotelsByDate(date: String): Flow<List<Hotel>> = hotelDao.getHotelsByDate(date)

    // 📌 **Ulaşım İşlemleri**
    suspend fun insertTransport(transport: Transport) = transportDao.insertTransport(transport)
    suspend fun deleteTransport(transport: Transport) = transportDao.deleteTransport(transport)
    fun getTransportsByDayPlan(dayPlanId: Int): Flow<List<Transport>> = transportDao.getTransportByDayPlan(dayPlanId)
    fun getTransportsByDate(date: String): Flow<List<Transport>> = transportDao.getTransportsByDate(date)

    // 📌 **Hatırlatıcı İşlemleri**
    suspend fun insertReminder(reminder: Reminder) = reminderDao.insertReminder(reminder)
    suspend fun deleteReminder(reminder: Reminder) = reminderDao.deleteReminder(reminder)
    fun getRemindersByDate(date: String): Flow<List<Reminder>> = reminderDao.getRemindersByDate(date)

    // 📌 **Araç Kiralama İşlemleri**
    suspend fun insertRent(rent: Rent) = rentDao.insertRent(rent)
    suspend fun deleteRent(rent: Rent) = rentDao.deleteRent(rent)
    fun getRentsByDayPlan(dayPlanId: Int): Flow<List<Rent>> = rentDao.getRentsByDayPlan(dayPlanId)
    fun getRentsByDate(date: String): Flow<List<Rent>> = rentDao.getRentsByDate(date)

    // 📌 **Müze İşlemleri**
    suspend fun insertMuseum(museum: Museum) = museumDao.insertMuseum(museum)
    suspend fun deleteMuseum(museum: Museum) = museumDao.deleteMuseum(museum)
    fun getMuseumsByDayPlan(dayPlanId: Int): Flow<List<Museum>> = museumDao.getMuseumsByDayPlan(dayPlanId)
    fun getMuseumsByDate(date: String): Flow<List<Museum>> = museumDao.getMuseumsByDate(date)

    // 📌 **Restoran İşlemleri**
    suspend fun insertRestaurant(restaurant: Restaurant) = restaurantDao.insertRestaurant(restaurant)
    suspend fun deleteRestaurant(restaurant: Restaurant) = restaurantDao.deleteRestaurant(restaurant)
    fun getRestaurantsByDayPlan(dayPlanId: Int): Flow<List<Restaurant>> = restaurantDao.getRestaurantsByDayPlan(dayPlanId)
    fun getRestaurantsByDate(date: String): Flow<List<Restaurant>> = restaurantDao.getRestaurantsByDate(date)

    // 📌 **Seçilen güne ait tüm aktiviteleri ID'ye göre getiren fonksiyon**
    fun getAllPlansByDayPlan(dayPlanId: Int): Flow<AllDayPlans> {
        val hotelsFlow = getHotelsByDayPlan(dayPlanId)
        val restaurantsFlow = getRestaurantsByDayPlan(dayPlanId)
        val transportsFlow = getTransportsByDayPlan(dayPlanId)
        val rentsFlow = getRentsByDayPlan(dayPlanId)
        val museumsFlow = getMuseumsByDayPlan(dayPlanId)

        return combine(
            hotelsFlow,
            restaurantsFlow,
            transportsFlow,
            rentsFlow,
            museumsFlow
        ) { hotels, restaurants, transports, rents, museums ->
            AllDayPlans(
                hotels = hotels,
                restaurants = restaurants,
                transports = transports,
                rents = rents,
                museums = museums
            )
        }
    }
    suspend fun deleteAllData(){
        noteDao.deleteAll()
        hotelDao.deleteAll()
        restaurantDao.deleteAll()
        museumDao.deleteAll()
        rentDao.deleteAll()
        transportDao.deleteAll()
        placeDao.deleteAll()
        cityDao.deleteAll()
        countryDao.deleteAll()
        dayPlanDao.deleteAll()

    }

    // 📌 **Seçilen güne ait tüm aktiviteleri TARİHE göre getiren fonksiyon**
    fun getAllPlansByDate(date: String): Flow<AllDayPlans> {
        val hotelsFlow = getHotelsByDate(date)
        val restaurantsFlow = getRestaurantsByDate(date)
        val transportsFlow = getTransportsByDate(date)
        val rentsFlow = getRentsByDate(date)
        val museumsFlow = getMuseumsByDate(date)

        return combine(
            hotelsFlow,
            restaurantsFlow,
            transportsFlow,
            rentsFlow,
            museumsFlow
        ) { hotels, restaurants, transports, rents, museums ->
            AllDayPlans(
                hotels = hotels,
                restaurants = restaurants,
                transports = transports,
                rents = rents,
                museums = museums
            )
        }
    }
}