package com.example.travellerfelix.bottomsheet

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.travellerfelix.data.local.database.TravelDatabase
import com.example.travellerfelix.data.local.model.DayPlan
import com.example.travellerfelix.data.local.model.Hotel
import com.example.travellerfelix.data.repository.TravelRepository
import com.example.travellerfelix.databinding.BottomSheetAddHotelBinding
import com.example.travellerfelix.viewmodel.CalendarViewModel
import com.example.travellerfelix.viewmodel.CalendarViewModelFactory
import com.example.travellerfelix.viewmodel.HotelViewModel
import com.example.travellerfelix.viewmodel.HotelViewModelFactory
import com.example.travellerfelix.viewmodel.SharedCalendarViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class HotelBottomSheet(private val city: String, private val country: String) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddHotelBinding? = null
    private val binding get() = _binding!!

    private lateinit var hotelViewModel: HotelViewModel
    private lateinit var calendarViewModel: CalendarViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = BottomSheetAddHotelBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val db = TravelDatabase.getDatabase(requireContext())
        val repo = TravelRepository(
            cityDao = db.cityDao(),
            dayPlanDao = db.dayPlanDao(),
            hotelDao = db.hotelDao(),
            transportDao = db.transportDao(),
            reminderDao = db.reminderDao(),
            rentDao = db.rentDao(),
            museumDao = db.museumDao(),
            restaurantDao = db.restaurantDao(),
            noteDao = db.noteDao(),
            placeDao = db.placeDao(),
            countryDao = db.countryDao()
        )

        hotelViewModel = ViewModelProvider(this, HotelViewModelFactory(repo))[HotelViewModel::class.java]
        calendarViewModel = ViewModelProvider(requireActivity(), CalendarViewModelFactory(db.dayPlanDao()))[CalendarViewModel::class.java]

        binding.hotelCheckInDate.setOnClickListener { showDatePicker(binding.hotelCheckInDate) }
        binding.hotelCheckOutDate.setOnClickListener { showDatePicker(binding.hotelCheckOutDate) }
        binding.hotelCheckInTime.setOnClickListener { showTimePicker(binding.hotelCheckInTime) }
        binding.hotelCheckOutTime.setOnClickListener { showTimePicker(binding.hotelCheckOutTime) }

        binding.saveButton.setOnClickListener {
            val hotelName = binding.hotelName.text.toString()
            val checkInDate = binding.hotelCheckInDate.text.toString()
            val checkOutDate = binding.hotelCheckOutDate.text.toString()
            val checkInTime = binding.hotelCheckInTime.text.toString()
            val checkOutTime = binding.hotelCheckOutTime.text.toString()

            if (hotelName.isBlank() || checkInDate.isBlank() || checkOutDate.isBlank() || checkInTime.isBlank() || checkOutTime.isBlank()) {
                Log.e("HotelBottomSheet", "⚠️ Eksik alanlar var!")
                return@setOnClickListener
            }

            checkOrCreateDayPlans(checkInDate, checkOutDate) { firstDayPlanId ->
                val hotel = Hotel(
                    dayPlanId = firstDayPlanId,
                    name = hotelName,
                    checkInDate = checkInDate,
                    checkOutDate = checkOutDate,
                    checkInTime = checkInTime,
                    checkOutTime = checkOutTime,
                    city = city,
                    country = country
                )

                hotelViewModel.insertHotel(hotel)
                calendarViewModel.generateCalendar()
                Log.d("HotelBottomSheet", "✅ Otel Kaydedildi: $hotelName | Tarih: $checkInDate - $checkOutDate")
                dismiss()
            }
        }

        binding.cancelButton.setOnClickListener { dismiss() }
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val datePicker = DatePickerDialog(
            requireContext(),
            { _, year, month, day ->
                val formatted = String.format("%04d-%02d-%02d", year, month + 1, day)
                editText.setText(formatted)
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePicker.show()
    }

    private fun showTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val timePicker = TimePickerDialog(
            requireContext(),
            { _, hour, minute ->
                val formatted = String.format("%02d:%02d", hour, minute)
                editText.setText(formatted)
            },
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        )
        timePicker.show()
    }

    private fun checkOrCreateDayPlans(startDate: String, endDate: String, callback: (Int) -> Unit) {
        val dayPlanDao = TravelDatabase.getDatabase(requireContext()).dayPlanDao()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        CoroutineScope(Dispatchers.IO).launch {
            val existingPlans = dayPlanDao.getDayPlansInRange(startDate, endDate).map { it.date }.toSet()
            val firstDayPlanId: Int
            calendar.time = sdf.parse(startDate)!!

            var assignedFirstId: Int? = null

            while (!calendar.time.after(sdf.parse(endDate)!!)) {
                val currentDate = sdf.format(calendar.time)

                if (!existingPlans.contains(currentDate)) {
                    val newPlan = DayPlan(date = currentDate, cityId = 1)
                    val insertedId = dayPlanDao.insertDayPlan(newPlan).toInt()
                    Log.d("HotelBottomSheet", "Yeni DayPlan Eklendi - Tarih: $currentDate")
                    if (assignedFirstId == null) assignedFirstId = insertedId
                } else {
                    val existingPlan = dayPlanDao.getDayPlansByDate(currentDate).firstOrNull()
                    if (assignedFirstId == null && existingPlan != null) assignedFirstId = existingPlan.id
                    Log.d("HotelBottomSheet", "Zaten Mevcut DayPlan - Tarih: $currentDate")
                }

                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            withContext(Dispatchers.Main) {
                callback(assignedFirstId ?: 1)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}