package com.example.travellerfelix.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travellerfelix.adapter.CalendarAdapter
import com.example.travellerfelix.adapter.DayPlanAdapter
import com.example.travellerfelix.bottomsheet.*
import com.example.travellerfelix.data.local.database.TravelDatabase
import com.example.travellerfelix.data.local.model.AllDayPlans
import com.example.travellerfelix.data.repository.TravelRepository
import com.example.travellerfelix.databinding.FragmentCalendarBinding
import com.example.travellerfelix.viewmodel.CalendarViewModel
import com.example.travellerfelix.viewmodel.CalendarViewModelFactory
import com.example.travellerfelix.viewmodel.SharedCalendarViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class CalendarFragment : Fragment(), CategorySelectionBottomSheet.OnCategorySelectedListener {

    private var _binding: FragmentCalendarBinding? = null
    private val binding get() = _binding!!

    private lateinit var calendarAdapter: CalendarAdapter
    private lateinit var dayPlanAdapter: DayPlanAdapter
    private lateinit var calendarViewModel: CalendarViewModel
    private lateinit var repository: TravelRepository
    private lateinit var sharedCalendarViewModel: SharedCalendarViewModel

    private var currentSelectedDate: String = getTodayDate()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCalendarBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val database = TravelDatabase.getDatabase(requireContext())
        repository = TravelRepository(
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

        val factory = CalendarViewModelFactory(database.dayPlanDao())
        calendarViewModel = ViewModelProvider(requireActivity(), factory)[CalendarViewModel::class.java]

        setupCalendarRecyclerView()
        setupDayPlanRecyclerView()
        setupCategorySelectorUI()

        sharedCalendarViewModel = ViewModelProvider(requireActivity())[SharedCalendarViewModel::class.java]

        // 🔁 DOTLARI ANLIK GÜNCELLE
        calendarViewModel.calendarDaysLive.observe(viewLifecycleOwner) { dayList ->
            calendarAdapter.submitList(dayList.toList()) {
                calendarAdapter.notifyDataSetChanged() // 🔁 adapter güncellensin diye
            }
        }

        // 🔄 Dışarıdan gelen refresh sinyali
        sharedCalendarViewModel.refreshSignal
            .collectWhenStarted(viewLifecycleOwner) { signal ->
                if (signal) {
                    calendarViewModel.generateCalendar()
                    Log.d("CalendarFragment", "📣 Dotlar güncellendi (refreshSignal ile).")

                    lifecycleScope.launch {
                        delay(200)
                        calendarViewModel.calendarDaysLive.value?.let {
                            calendarAdapter.submitList(it.toList())
                        }
                    }
                }
            }

        calendarViewModel.generateCalendar()
        loadDayPlansForDate(currentSelectedDate)



        binding.setCalendar.setOnClickListener {
            val locationFragment = LocationSelectionFragment(object :
                LocationSelectionFragment.OnLocationSelectedListener {
                override fun onLocationSelected(selectedCountry: String, selectedCity: String) {
                    binding.cityAndCountryText.text = "$selectedCity / $selectedCountry"
                    val categoryBottomSheet =
                        CategorySelectionBottomSheet(this@CalendarFragment, currentSelectedDate)
                    categoryBottomSheet.show(parentFragmentManager, "CategorySelectionBottomSheet")
                }
            })
            locationFragment.show(parentFragmentManager, "LocationSelectionBottomSheet")
        }

        binding.settings.setOnClickListener {
            SettingsBottomSheet().show(parentFragmentManager,"SettingsBottomSheet")
        }

        binding.addEventButton.setOnClickListener {
            val locationFragment = LocationSelectionFragment(object:LocationSelectionFragment.OnLocationSelectedListener{
                override fun onLocationSelected(selectedCountry: String, selectedCity: String) {
                    binding.cityAndCountryText.text = "$selectedCity / $selectedCountry"
                    val categoryBottomSheet = CategorySelectionBottomSheet(this@CalendarFragment, currentSelectedDate)
                    categoryBottomSheet.show(parentFragmentManager,"CategorySelectionBottomSheet")
                }
            })
            locationFragment.show(parentFragmentManager,"LocationSelectionBottomSheet")
        }
        binding.emptyCalendarText.setOnClickListener {
            val locationFragment = LocationSelectionFragment(object:LocationSelectionFragment.OnLocationSelectedListener{
                override fun onLocationSelected(selectedCountry: String, selectedCity: String) {
                    binding.cityAndCountryText.text = "$selectedCity / $selectedCountry"
                    val categoryBottomSheet = CategorySelectionBottomSheet(this@CalendarFragment, currentSelectedDate)
                    categoryBottomSheet.show(parentFragmentManager,"CategorySelectionBottomSheet")
                }
            })
            locationFragment.show(parentFragmentManager,"LocationSelectionBottomSheet")
        }

    }
    private fun updateEmptyStateUI(allPlans: AllDayPlans) {
        val hasAnyData = allPlans.hotels.isNotEmpty()
                || allPlans.restaurants.isNotEmpty()
                || allPlans.transports.isNotEmpty()
                || allPlans.rents.isNotEmpty()
                || allPlans.museums.isNotEmpty()

        if (hasAnyData) {
            binding.calendarEmptyView.visibility = View.GONE
            binding.emptyCalendarText.visibility = View.GONE
            binding.addEventButton.visibility = View.GONE
        } else {
            binding.calendarEmptyView.visibility = View.VISIBLE
            binding.emptyCalendarText.visibility = View.VISIBLE
            binding.addEventButton.visibility = View.VISIBLE
        }
    }

    private fun setupCalendarRecyclerView() {
        calendarAdapter = CalendarAdapter().apply {
            setOnDateClickListener { date ->
                currentSelectedDate = date
                val selectedCategory = binding.selectedCategory.text.toString()
                if (selectedCategory == "All") {
                    loadDayPlansForDate(date)
                } else {
                    loadFilteredDayPlansForDate(date, selectedCategory)
                }
            }
        }
        binding.dateRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.dateRecyclerView.adapter = calendarAdapter
    }

    private fun setupDayPlanRecyclerView() {
        dayPlanAdapter = DayPlanAdapter { selectedItem ->
            binding.cityAndCountryText.text = "${selectedItem.city} / ${selectedItem.country}"
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = dayPlanAdapter
    }

    private fun setupCategorySelectorUI() {
        val actions = mapOf(
            binding.categoryOptionAll to "All",
            binding.categoryOptionHotel to "Hotel",
            binding.categoryOptionRent to "Rent a Car",
            binding.categoryOptionMuseum to "Museum",
            binding.categoryOptionTransport to "Transport",
            binding.categoryOptionRestaurant to "Restaurant"
        )
        actions.forEach { (view, category) ->
            view.setOnClickListener {
                binding.selectedCategory.text = category
                binding.categoryDropdownMenu.visibility = View.GONE
                refreshPlansForDate(currentSelectedDate)
            }
        }

        binding.categoryButton.setOnClickListener {
            binding.categoryDropdownMenu.visibility =
                if (binding.categoryDropdownMenu.visibility == View.GONE) View.VISIBLE else View.GONE
        }
    }

    private fun refreshPlansForDate(date: String) {
        val selectedCategory = binding.selectedCategory.text.toString()
        if (selectedCategory == "All") {
            loadDayPlansForDate(date)
        } else {
            loadFilteredDayPlansForDate(date, selectedCategory)
        }
    }

    private fun loadDayPlansForDate(date: String) {
        Log.d("CalendarDebug", "loadDayPlansForDate çağrıldı: $date")
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val allPlans = AllDayPlans(
                    hotels = repository.getHotelsByDate(date).firstOrNull() ?: emptyList(),
                    rents = repository.getRentsByDate(date).firstOrNull() ?: emptyList(),
                    museums = repository.getMuseumsByDate(date).firstOrNull() ?: emptyList(),
                    restaurants = repository.getRestaurantsByDate(date).firstOrNull() ?: emptyList(),
                    transports = repository.getTransportsByDate(date).firstOrNull() ?: emptyList()
                )
                withContext(Dispatchers.Main) {
                    dayPlanAdapter.submitList(allPlans)
                    updateEmptyStateUI(allPlans)
                }
            } catch (e: Exception) {
                Log.e("CalendarDebug", "Veri çekilirken hata oluştu: ${e.localizedMessage}")
            }
        }
    }

    private fun loadFilteredDayPlansForDate(date: String, category: String) {
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                val plans = when (category) {
                    "Hotel" -> AllDayPlans(hotels = repository.getHotelsByDate(date).firstOrNull() ?: emptyList())
                    "Rent a Car" -> AllDayPlans(rents = repository.getRentsByDate(date).firstOrNull() ?: emptyList())
                    "Museum" -> AllDayPlans(museums = repository.getMuseumsByDate(date).firstOrNull() ?: emptyList())
                    "Transport" -> AllDayPlans(transports = repository.getTransportsByDate(date).firstOrNull() ?: emptyList())
                    "Restaurant" -> AllDayPlans(restaurants = repository.getRestaurantsByDate(date).firstOrNull() ?: emptyList())
                    else -> AllDayPlans()
                }
                withContext(Dispatchers.Main) {
                    dayPlanAdapter.submitList(plans)
                    updateEmptyStateUI(plans)
                }
            } catch (e: Exception) {
                Log.e("DB_TEST", "⚠️ Filtreli veri yükleme hatası: ${e.localizedMessage}")
            }
        }
    }

    override fun onCategorySelected(category: String, selectedDate: String) {
        val parts = binding.cityAndCountryText.text.toString().split("/")
        val city = parts.getOrNull(0)?.trim() ?: ""
        val country = parts.getOrNull(1)?.trim() ?: ""

        val bottomSheet = when (category) {
            "Hotel" -> HotelBottomSheet( city,country)
            "Rent a Car" -> RentBottomSheet(selectedDate, city, country)
            "Museum" -> MuseumBottomSheet(city, country)
            "Transport" -> TransportBottomSheet(city, country)
            "Restaurant" -> RestaurantBottomSheet(city, country)
            else -> null
        }

        bottomSheet?.show(parentFragmentManager, "AddCategoryBottomSheet")
    }

    override fun onResume() {
        super.onResume()
        calendarViewModel.generateCalendar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getTodayDate(): String {
        val calendar = Calendar.getInstance()
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return dateFormat.format(calendar.time)
    }

    // 🔄 Flow'u lifecycle-aware collect et
    fun <T> Flow<T>.collectWhenStarted(
        owner: androidx.lifecycle.LifecycleOwner,
        collector: suspend (T) -> Unit
    ) {
        owner.lifecycleScope.launch {
            owner.lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                collect { collector(it) }
            }
        }
    }
}