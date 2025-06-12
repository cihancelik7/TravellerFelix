package com.example.travellerfelix.data.repository

import com.example.travellerfelix.data.local.dao.HotelDao
import com.example.travellerfelix.data.local.dao.MuseumDao
import com.example.travellerfelix.data.local.dao.RentDao
import com.example.travellerfelix.data.local.dao.RestaurantDao
import com.example.travellerfelix.data.local.dao.TransportDao
import com.example.travellerfelix.data.local.model.TomorrowPlanItem

import java.time.LocalDate

class TomorrowPlanRepository(
    private val hotelDao: HotelDao,
    private val restaurantDao: RestaurantDao,
    private val transportDao: TransportDao,
    private val rentDao: RentDao,
    private val museumDao: MuseumDao
) {
    suspend fun getTomorrowPlans(): List<TomorrowPlanItem> {
        val tomorrow = LocalDate.now().plusDays(1).toString()

        val hotelPlans = hotelDao.getHotelsForDate(tomorrow).map {
            TomorrowPlanItem(it.city, "Otel", it.checkInTime, it.name)
        }

        val restaurantPlans = restaurantDao.getRestaurantsForDate(tomorrow).map {
            TomorrowPlanItem(it.city, "Restoran", it.reservationTime, it.name)
        }

        val transportPlans = transportDao.getTransportForDate(tomorrow).map {
            TomorrowPlanItem(it.city, "Ulaşım", it.reservationTime, it.transportType)
        }

        val rentPlans = rentDao.getRentsForDate(tomorrow).map {
            TomorrowPlanItem(it.city, "Araç Kiralama", it.pickUpTime, it.rentalCompany)
        }

        val museumPlans = museumDao.getMuseumForDate(tomorrow).map {
            TomorrowPlanItem(it.city, "Müze", it.visitTime, it.name)
        }

        return (hotelPlans + restaurantPlans + transportPlans + rentPlans + museumPlans)
            .sortedBy { it.time }
    }
}