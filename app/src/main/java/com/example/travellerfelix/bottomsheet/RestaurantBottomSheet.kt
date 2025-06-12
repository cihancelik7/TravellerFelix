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
import com.example.travellerfelix.data.local.model.Restaurant
import com.example.travellerfelix.data.repository.TravelRepository
import com.example.travellerfelix.databinding.BottomSheetAddRestaurantBinding
import com.example.travellerfelix.viewmodel.CalendarViewModel
import com.example.travellerfelix.viewmodel.CalendarViewModelFactory
import com.example.travellerfelix.viewmodel.RestaurantViewModel
import com.example.travellerfelix.viewmodel.RestaurantViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*

class RestaurantBottomSheet(private val city:String, private val country:String) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddRestaurantBinding? = null
    private val binding get() = _binding!!

    private lateinit var restaurantViewModel: RestaurantViewModel
    private lateinit var calendarViewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddRestaurantBinding.inflate(inflater, container, false)
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

        val restaurantFactory = RestaurantViewModelFactory(repo)
        restaurantViewModel = ViewModelProvider(this, restaurantFactory)[RestaurantViewModel::class.java]

        val calendarFactory = CalendarViewModelFactory(db.dayPlanDao())
        calendarViewModel = ViewModelProvider(requireActivity(), calendarFactory)[CalendarViewModel::class.java]

        // 📌 **Zaman Seçimi için Tıklanabilir Alanlar**
        binding.restaurantReservationDate.setOnClickListener { showDatePicker(binding.restaurantReservationDate) }
        binding.restaurantReservationTime.setOnClickListener { showTimePicker(binding.restaurantReservationTime) }
        binding.restaurantOpenCloseTime.setOnClickListener { showTimePicker(binding.restaurantOpenCloseTime) }

        // 📌 **Kaydetme işlemi**
        binding.saveButton.setOnClickListener {
            val restaurantName = binding.restaurantName.text.toString()
            val reservationDate = binding.restaurantReservationDate.text.toString()
            val reservationTime = binding.restaurantReservationTime.text.toString()
            val openCloseTime = binding.restaurantOpenCloseTime.text.toString()

            if (restaurantName.isBlank() || reservationTime.isBlank() || openCloseTime.isBlank()) {
                Log.e("RestaurantBottomSheet", "Eksik bilgiler var. Kayıt başarısız!")
                return@setOnClickListener
            }

            // 📌 **Seçilen rezervasyon saatine göre DayPlan oluşturup kaydet**
            checkOrCreateDayPlan(reservationDate) { dayPlanId ->
                val newRestaurant = Restaurant(
                    dayPlanId = dayPlanId, // ✅ ID otomatik oluşacak, elle eklemiyoruz!
                    name = restaurantName,
                    reservationDate = reservationDate,
                    reservationTime = reservationTime,
                    openCloseTime = openCloseTime,
                    city = city,
                    country = country
                )

                restaurantViewModel.insertRestaurant(newRestaurant)

                // 📌 **Takvimi güncelle**
                calendarViewModel.generateCalendar()

                dismiss()
            }
        }

        binding.cancelButton.setOnClickListener { dismiss() }
    }
    private fun showDatePicker(editText: EditText){
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            {_, selectedYear, selectedMonth, selectedDay ->
                val formattedDate = String.format(Locale.getDefault(),"%04d-%02d-%02d",selectedYear,selectedMonth+ 1,selectedDay)
                editText.setText(formattedDate)
            },
            year, month,day
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

    // 📌 **Eğer o tarihte `DayPlan` yoksa oluştur, varsa mevcut ID’yi getir (Hotel, Rent ve Museum ile Aynı)**
    private fun checkOrCreateDayPlan(date: String, callback: (Int) -> Unit) {
        val dayPlanDao = TravelDatabase.getDatabase(requireContext()).dayPlanDao()

        CoroutineScope(Dispatchers.IO).launch {
            val existingPlan = dayPlanDao.getDayPlansByDate(date).firstOrNull()

            if (existingPlan == null) {
                val newDayPlan = DayPlan(date = date, cityId = 1)
                val newId = dayPlanDao.insertDayPlan(newDayPlan).toInt()
                Log.d("RestaurantBottomSheet", "Yeni DayPlan Eklendi - Tarih: $date, ID: $newId")

                withContext(Dispatchers.Main) {
                    callback(newId)
                }
            } else {
                Log.d("RestaurantBottomSheet", "Zaten mevcut DayPlan - Tarih: $date, ID: ${existingPlan.id}")
                withContext(Dispatchers.Main) {
                    callback(existingPlan.id)
                }
            }
        }
    }
}