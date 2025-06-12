package com.example.travellerfelix.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.travellerfelix.data.local.dao.DayPlanDao
import com.example.travellerfelix.data.local.model.DayItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class CalendarViewModel(private val dayPlanDao: DayPlanDao) : ViewModel() {

    private val _calendarDays = MutableStateFlow<List<DayItem>>(emptyList())
    val calendarDays: StateFlow<List<DayItem>> get() = _calendarDays

    // 🔁 Ekstra LiveData ile anında güncellemeyi zorlamak için:
    private val _calendarDaysLive = MutableLiveData<List<DayItem>>()
    val calendarDaysLive: LiveData<List<DayItem>> get() = _calendarDaysLive

    fun generateCalendar() {
        Log.d("CalendarViewModel", "generateCalendar() çağrıldı!")

        viewModelScope.launch(Dispatchers.IO) {
            val calendarList = mutableListOf<DayItem>()
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val sdfDayName = SimpleDateFormat("EEE", Locale.getDefault())

            val calendar = Calendar.getInstance()
            val startDate = sdf.format(calendar.time)
            calendar.add(Calendar.DAY_OF_MONTH, 30)
            val endDate = sdf.format(calendar.time)

            val existingPlans = dayPlanDao.getDayPlansInRange(startDate, endDate)
                .map { it.date }
                .toSet()

            calendar.time = sdf.parse(startDate)!!

            for (i in 0..30) {
                val date = sdf.format(calendar.time)
                val dayName = sdfDayName.format(calendar.time).uppercase(Locale.getDefault())
                val isAvailable = existingPlans.contains(date)

                calendarList.add(DayItem(date, dayName, isAvailable))
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            Log.d("CalendarViewModel", "Takvim Güncellendi: ${calendarList.size} gün var!")
            _calendarDays.emit(calendarList)
            _calendarDaysLive.postValue(calendarList)
        }

    }
}