package com.example.travellerfelix.bottomsheet

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.ViewModelProvider
import com.example.travellerfelix.data.local.database.TravelDatabase
import com.example.travellerfelix.data.local.model.DayPlan
import com.example.travellerfelix.data.local.model.Rent
import com.example.travellerfelix.data.repository.TravelRepository
import com.example.travellerfelix.databinding.BottomSheetAddRentBinding
import com.example.travellerfelix.viewmodel.CalendarViewModel
import com.example.travellerfelix.viewmodel.CalendarViewModelFactory
import com.example.travellerfelix.viewmodel.RentViewModel
import com.example.travellerfelix.viewmodel.RentViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RentBottomSheet(
    private val selectedDate: String,
    private val city: String,
    private val country: String
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetAddRentBinding? = null
    private val binding get() = _binding!!

    private lateinit var rentViewModel: RentViewModel
    private lateinit var calendarViewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAddRentBinding.inflate(inflater, container, false)
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

        rentViewModel = ViewModelProvider(this, RentViewModelFactory(repo))[RentViewModel::class.java]
        calendarViewModel = ViewModelProvider(requireActivity(), CalendarViewModelFactory(db.dayPlanDao()))[CalendarViewModel::class.java]

        binding.rentStartDate.setOnClickListener { showDatePicker(binding.rentStartDate) }
        binding.rentEndDate.setOnClickListener { showDatePicker(binding.rentEndDate) }

        binding.saveButton.setOnClickListener {
            val rentalCompany = binding.carRentalBrandName.text.toString()
            val carModel = binding.carModel.text.toString()
            val pickupDate = binding.rentStartDate.text.toString()
            val dropOffDate = binding.rentEndDate.text.toString()
            val pickupTime = binding.pickupTime.text.toString()
            val dropOffTime = binding.dropOffTime.text.toString()
            val pickupLocation = binding.pickupLocation.text.toString()
            val dropOffLocation = binding.dropOffLocation.text.toString()

            if (rentalCompany.isBlank() || pickupDate.isBlank() || dropOffDate.isBlank()) {
                Log.e("RentBottomSheet", "Eksik Bilgiler Var, Kayit Basarisiz!")
                return@setOnClickListener
            }

            checkOrCreateDayPlans(pickupDate, dropOffDate) { firstDayPlanId ->
                val newRent = Rent(
                    dayPlanId = firstDayPlanId,
                    rentalCompany = rentalCompany,
                    pickupDate = pickupDate,
                    dropOffDate = dropOffDate,
                    pickUpTime = pickupTime,
                    dropOffTime = dropOffTime,
                    pickupLocation = pickupLocation,
                    dropOffLocation = dropOffLocation,
                    carModel = carModel,
                    city = city,
                    country = country
                )
                rentViewModel.insertRent(newRent)
                calendarViewModel.generateCalendar()
                Log.d("RentBottomSheet", "✅ Kiralama Kaydedildi: $rentalCompany | Tarih: $pickupDate - $dropOffDate")
                dismiss()
            }
        }

        binding.cancelButton.setOnClickListener { dismiss() }
    }

    private fun showDatePicker(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(requireContext(), { _, selectedYear, selectedMonth, selectedDay ->
            val formattedDate = String.format("%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay)
            editText.setText(formattedDate)
        }, year, month, day)
        datePicker.show()
    }

    private fun checkOrCreateDayPlans(startDate: String, endDate: String, callback: (Int) -> Unit) {
        val dayPlanDao = TravelDatabase.getDatabase(requireContext()).dayPlanDao()
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val calendar = Calendar.getInstance()

        CoroutineScope(Dispatchers.IO).launch {
            val existingPlans = dayPlanDao.getDayPlansInRange(startDate, endDate).map { it.date }.toSet()
            var firstDayPlanId: Int? = null

            calendar.time = sdf.parse(startDate)!!
            while (!calendar.time.after(sdf.parse(endDate)!!)) {
                val currentDate = sdf.format(calendar.time)
                if (!existingPlans.contains(currentDate)) {
                    val newPlan = DayPlan(date = currentDate, cityId = 1)
                    val insertedId = dayPlanDao.insertDayPlan(newPlan).toInt()
                    Log.d("RentBottomSheet", "Yeni DayPlan Eklendi - Tarih: $currentDate")
                    if (firstDayPlanId == null) firstDayPlanId = insertedId
                } else {
                    val existingPlan = dayPlanDao.getDayPlansByDate(currentDate).firstOrNull()
                    if (firstDayPlanId == null && existingPlan != null) {
                        firstDayPlanId = existingPlan.id
                    }
                    Log.d("RentBottomSheet", "Zaten Mevcut DayPlan - Tarih: $currentDate")
                }
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            withContext(Dispatchers.Main) {
                callback(firstDayPlanId ?: 1)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}