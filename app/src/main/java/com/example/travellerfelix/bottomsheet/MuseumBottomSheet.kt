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
import com.example.travellerfelix.data.local.model.Museum
import com.example.travellerfelix.data.repository.TravelRepository
import com.example.travellerfelix.databinding.BottomSheetAddMuseumBinding
import com.example.travellerfelix.viewmodel.CalendarViewModel
import com.example.travellerfelix.viewmodel.CalendarViewModelFactory
import com.example.travellerfelix.viewmodel.MuseumViewModel
import com.example.travellerfelix.viewmodel.MuseumViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class MuseumBottomSheet(private val city: String, private val country: String) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddMuseumBinding? = null
    private val binding get() = _binding!!

    private lateinit var museumViewModel: MuseumViewModel
    private lateinit var calendarViewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddMuseumBinding.inflate(inflater, container, false)
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

        // **ViewModel başlat**
        val museumFactory = MuseumViewModelFactory(repo)
        museumViewModel = ViewModelProvider(this, museumFactory)[MuseumViewModel::class.java]

        val calendarFactory = CalendarViewModelFactory(db.dayPlanDao())
        calendarViewModel = ViewModelProvider(requireActivity(), calendarFactory)[CalendarViewModel::class.java]

        // 📌 **Kullanıcının tarih seçmesini sağla**
        binding.museumVisitDate.setOnClickListener { showDatePicker(binding.museumVisitDate) }
        binding.museumVisitTime.setOnClickListener { showTimePicker(binding.museumVisitTime) }
        binding.museumOpenCloseTime.setOnClickListener { showTimePicker(binding.museumOpenCloseTime) }

        // 📌 **Kaydetme işlemi**
        binding.saveButton.setOnClickListener {
            val museumName = binding.museumName.text.toString()
            val visitDate = binding.museumVisitDate.text.toString()
            val visitTime = binding.museumVisitTime.text.toString()
            val openCloseTime = binding.museumOpenCloseTime.text.toString()

            if (museumName.isBlank() || visitDate.isBlank() || visitTime.isBlank() || openCloseTime.isBlank()) {
                Log.e("MuseumBottomSheet", "Eksik bilgiler var. Kayıt başarısız!")
                return@setOnClickListener
            }

            // 📌 **Seçilen tarih için `DayPlan` oluştur veya mevcut olanı kullan**
            checkOrCreateDayPlan(visitDate) { dayPlanId ->
                val newMuseum = Museum(
                    name = museumName,
                    dayPlanId = dayPlanId, // DayPlan ID buradan geliyor
                    visitDate = visitDate,
                    visitTime = visitTime,
                    openCloseTime = openCloseTime,
                    city = city,
                    country = country
                )

                CoroutineScope(Dispatchers.IO).launch {
                    museumViewModel.insertMuseum(newMuseum)

                    withContext(Dispatchers.Main) {
                        Log.d("MuseumBottomSheet", "✅ Müze kaydedildi: ${newMuseum.name} | Tarih: ${newMuseum.visitDate}")
                        // 📌 **Takvimi güncelle!**
                        calendarViewModel.generateCalendar()
                        dismiss()
                    }
                }
            }
        }

        binding.cancelButton.setOnClickListener { dismiss() }
    }

    // 📌 **Kullanıcının tarih seçmesini sağlayan fonksiyon**
    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
                editText.setText(formattedDate)
            },
            year,
            month,
            day
        )

        datePickerDialog.show()
    }

    // 📌 **Kullanıcının saat seçmesini sağlayan fonksiyon**
    private fun showTimePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, selectedHour, selectedMinute ->
                val formattedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
                editText.setText(formattedTime)
            },
            hour,
            minute,
            true
        )

        timePickerDialog.show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    // 📌 **Eğer o tarihte `DayPlan` yoksa oluştur, varsa mevcut ID’yi getir (Hotel ve Rent ile Aynı)**
    private fun checkOrCreateDayPlan(date: String, callback: (Int) -> Unit) {
        val dayPlanDao = TravelDatabase.getDatabase(requireContext()).dayPlanDao()

        CoroutineScope(Dispatchers.IO).launch {
            val existingPlan = dayPlanDao.getDayPlansByDate(date).firstOrNull()

            if (existingPlan == null) {
                val newDayPlan = DayPlan(date = date, cityId = 1)
                val newId = dayPlanDao.insertDayPlan(newDayPlan).toInt()
                Log.d("MuseumBottomSheet", "Yeni DayPlan Eklendi - Tarih: $date, ID: $newId")

                withContext(Dispatchers.Main) {
                    callback(newId)
                }
            } else {
                Log.d("MuseumBottomSheet", "Zaten mevcut DayPlan - Tarih: $date, ID: ${existingPlan.id}")
                withContext(Dispatchers.Main) {
                    callback(existingPlan.id)
                }
            }
        }
    }
}