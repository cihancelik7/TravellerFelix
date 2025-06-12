package com.example.travellerfelix.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import com.example.travellerfelix.R
import com.example.travellerfelix.data.local.database.TravelDatabase
import com.example.travellerfelix.data.repository.TravelRepository
import com.example.travellerfelix.viewmodel.CalendarViewModel
import com.example.travellerfelix.viewmodel.DayPlanViewModel
import com.example.travellerfelix.viewmodel.DayPlanViewModelFactory
import com.example.travellerfelix.viewmodel.SharedCalendarViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SettingsBottomSheet : BottomSheetDialogFragment() {

    private val calendarViewModel: CalendarViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.fragment_settings_bottom_sheet, container, false)

        val context = requireContext()
        val database = TravelDatabase.getDatabase(context)
        val repository = TravelRepository(
            cityDao = database.cityDao(),
            dayPlanDao = database.dayPlanDao(),
            hotelDao = database.hotelDao(),
            transportDao = database.transportDao(),
            reminderDao = database.reminderDao(),
            rentDao = database.rentDao(),
            museumDao = database.museumDao(),
            restaurantDao = database.restaurantDao(),
            noteDao = database.noteDao(),
            placeDao = database.placeDao(),
            countryDao = database.countryDao()
        )

        val factory = DayPlanViewModelFactory(repository)
        val travelViewModel = ViewModelProvider(requireActivity(), factory)[DayPlanViewModel::class.java]
        val sharedCalendarViewModel = ViewModelProvider(requireActivity())[SharedCalendarViewModel::class.java]

        view.findViewById<TextView>(R.id.clearData).setOnClickListener {
            val clearDataSheet = ClearDataBottomSheet {
                travelViewModel.deleteAllData()
                sharedCalendarViewModel.sendRefreshSignal() // 📢 Dotları yenile
                Toast.makeText(context, "Veriler silindi.", Toast.LENGTH_SHORT).show()
            }
            clearDataSheet.show(parentFragmentManager, "ClearDataBottomSheet")
        }

        view.findViewById<TextView>(R.id.feedback).setOnClickListener {
            val feedbackSheet = FeedbackBottomSheet()
            feedbackSheet.show(parentFragmentManager, "FeedbackBottomSheet")
        }

        view.findViewById<TextView>(R.id.about).setOnClickListener {
            val aboutSheet = AboutBottomSheet()
            aboutSheet.show(parentFragmentManager, "AboutBottomSheet")
        }

        view.findViewById<TextView>(R.id.changeLanguage).setOnClickListener {
            val languageSheet = LanguageBottomSheet()
            languageSheet.show(parentFragmentManager, "LanguageBottomSheet")
        }

        return view
    }
}