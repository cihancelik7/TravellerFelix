package com.example.travellerfelix.bottomsheet

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.coordinatorlayout.widget.CoordinatorLayout.DispatchChangeEvent
import androidx.lifecycle.ViewModelProvider
import com.example.travellerfelix.data.local.database.TravelDatabase
import com.example.travellerfelix.data.local.model.DayPlan
import com.example.travellerfelix.data.local.model.Transport
import com.example.travellerfelix.data.repository.TravelRepository
import com.example.travellerfelix.databinding.BottomSheetAddTransportBinding
import com.example.travellerfelix.viewmodel.CalendarViewModel
import com.example.travellerfelix.viewmodel.CalendarViewModelFactory
import com.example.travellerfelix.viewmodel.HotelViewModel
import com.example.travellerfelix.viewmodel.TransportViewModel
import com.example.travellerfelix.viewmodel.TransportViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.Locale
import kotlin.math.min

class TransportBottomSheet(private val city:String, private val country:String): BottomSheetDialogFragment() {

    private var _binding : BottomSheetAddTransportBinding? = null
    private val binding get() = _binding!!

    private lateinit var transportViewModel: TransportViewModel
    private lateinit var calendarViewModel: CalendarViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = BottomSheetAddTransportBinding.inflate(inflater,container,false)
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
        // viewmodel baslat
        val transportFactory = TransportViewModelFactory(repo)
        transportViewModel = ViewModelProvider(this,transportFactory)[TransportViewModel::class.java]

        val calendarFactory = CalendarViewModelFactory(db.dayPlanDao())
        calendarViewModel = ViewModelProvider(requireActivity(),calendarFactory)[CalendarViewModel::class.java]

        // kullanicinin tarih secmesini sagla
        binding.ticketDate.setOnClickListener { showDatePicker(binding.ticketDate) }
        binding.reservationTime.setOnClickListener { showTimePicker(binding.reservationTime) }
        binding.transportDepartureTime.setOnClickListener { showTimePicker(binding.transportDepartureTime) }

            // kaydetme islemi
        binding.saveButton.setOnClickListener {
            val transportType = if (binding.transportTypeSpinner.selectedItem.toString()=="Other"){
                binding.otherTransportType.text.toString()
            }else{
                binding.transportTypeSpinner.selectedItem.toString()
            }

            val ticketName = binding.ticketName.text.toString()
            val ticketDate = binding.ticketDate.text.toString()
            val reservationTime = binding.reservationTime.text.toString()
            val departureTime = binding.transportDepartureTime.text.toString()

            if (transportType.isBlank() || ticketName.isBlank() || ticketDate.isBlank() || departureTime.isBlank()){
                Log.e("TransportBottomSheet","Eksik Bilgiler Var, Kayit Basarisiz!")
                return@setOnClickListener
            }
            checkOrCreateDayPlan(ticketDate){dayPlanId ->
                val newTransport = Transport(
                    transportType = transportType,
                    ticketName = ticketName,
                    dayPlanId = dayPlanId,
                    ticketDate = ticketDate,
                    reservationTime = reservationTime,
                    departureTime = departureTime,
                    city = city,
                    country = country
                )
                transportViewModel.insertTransport(newTransport)

                calendarViewModel.generateCalendar()
                dismiss()
            }
        }
        binding.cancelButton.setOnClickListener { dismiss() }
    }
    private fun showTimePicker(editText: EditText){
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            {_, selectedHour,selectedMinute ->
                val formattedTime = String.format("%02d:%02d",selectedHour,selectedMinute)
                editText.setText(formattedTime)
            },
            hour,minute,true
        )
        timePickerDialog.show()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun checkOrCreateDayPlan(date: String, callback: (Int) -> Unit) {
        val dayPlanDao = TravelDatabase.getDatabase(requireContext()).dayPlanDao()

        CoroutineScope(Dispatchers.IO).launch {
            val existingPlan = dayPlanDao.getDayPlansByDate(date).firstOrNull()

            if (existingPlan == null) {
                val newDayPlan = DayPlan(date = date, cityId = 1)
                val newId = dayPlanDao.insertDayPlan(newDayPlan).toInt()
                Log.d("TransportBottomSheet", "Yeni DayPlan Eklendi - Tarih: $date, ID: $newId")

                withContext(Dispatchers.Main) {
                    callback(newId)
                }
            } else {
                Log.d("TransportBottomSheet", "Zaten mevcut DayPlan - Tarih: $date, ID: ${existingPlan.id}")
                withContext(Dispatchers.Main) {
                    callback(existingPlan.id)
                }
            }
        }
    }

}