package com.example.travellerfelix.data.repository

import com.example.travellerfelix.data.local.dao.CityDao
import com.example.travellerfelix.data.local.model.City
import kotlinx.coroutines.flow.Flow

class CityRepository (private val cityDao: CityDao){

    fun getCitiesCountry(countryId: Int): Flow<List<City>> = cityDao.getCitiesByCountry(countryId)


    suspend fun insertCity(city:City){
        cityDao.insertCity(city)
    }
    suspend fun updateCityDate(cityId: Int,date:String){
        cityDao.updateCityDate(cityId,date)
    }



}