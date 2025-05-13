package com.example.travellerfelix.data.repository

import com.example.travellerfelix.data.local.dao.CountryDao
import com.example.travellerfelix.data.local.model.Country
import kotlinx.coroutines.flow.Flow

class CountryRepository(private val countryDao: CountryDao) {
    fun getAllCountries(): Flow<List<Country>> = countryDao.getAllCountries()

    suspend fun insertCountry(country: Country){
        countryDao.insertCountry(country)
    }
    suspend fun updateCountryDate(countryId: Int, date: String){
        countryDao.updateCountryDate(countryId,date)
    }
}