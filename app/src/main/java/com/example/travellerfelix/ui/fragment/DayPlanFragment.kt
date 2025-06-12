package com.example.travellerfelix.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travellerfelix.adapter.DayPlanAdapter
import com.example.travellerfelix.data.local.database.TravelDatabase
import com.example.travellerfelix.data.repository.TravelRepository
import com.example.travellerfelix.databinding.FragmentDayPlanBinding
import com.example.travellerfelix.viewmodel.DayPlanViewModel
import com.example.travellerfelix.viewmodel.DayPlanViewModelFactory
import kotlinx.coroutines.launch

class DayPlanFragment : Fragment() {

    private var _binding: FragmentDayPlanBinding? = null
    private val binding get() = _binding!!

    private lateinit var dayPlanAdapter: DayPlanAdapter
    private val dayPlanViewModel: DayPlanViewModel by viewModels {
        val db = TravelDatabase.getDatabase(requireContext())
        val repository = TravelRepository(
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
        DayPlanViewModelFactory(repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDayPlanBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        observeDayPlans()

        // 📌 Kullanıcı tarih seçtiğinde ilgili günün verilerini yükle
        binding.dateRecyclerView.setOnClickListener {
            val selectedDayPlanId = 1 // Örneğin, seçilen tarihin dayPlanId’si (Gerçek ID kullanılacak)
            dayPlanViewModel.loadDayPlans(selectedDayPlanId)
        }
    }

    private fun setupRecyclerView() {
        dayPlanAdapter = DayPlanAdapter { selectedItem ->
            // Bu fragment'ta cityId / countryId gösterilmiyor, sadece log yaz:
            Log.d("DayPlanFragment", "Seçilen plan konumu: ${selectedItem.city}, ${selectedItem.country}")
        }

        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = dayPlanAdapter
        }
    }

    private fun observeDayPlans() {
        lifecycleScope.launch {
            dayPlanViewModel.dayPlans.collect { allPlans ->
                allPlans?.let {
                    dayPlanAdapter.submitList(it)
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}