package com.example.travellerfelix.ui.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.travellerfelix.R
import com.example.travellerfelix.data.remote.GeoNamesApiService
import com.example.travellerfelix.data.repository.LocationRepository
import com.example.travellerfelix.databinding.FragmentLocationSelectionBinding
import com.example.travellerfelix.viewmodel.LocationViewModel
import com.example.travellerfelix.viewmodel.LocationViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LocationSelectionFragment(private val listener: OnLocationSelectedListener) : BottomSheetDialogFragment() {

    interface OnLocationSelectedListener {
        fun onLocationSelected(selectedCountry: String, selectedCity: String)
    }

    private var _binding: FragmentLocationSelectionBinding? = null
    private val binding get() = _binding!!

    // ✅ Retrofit API servisini oluştur
    private val apiService: GeoNamesApiService = Retrofit.Builder()
        .baseUrl("http://api.geonames.org/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(GeoNamesApiService::class.java)

    // ✅ ViewModel’i oluştur
    private val viewModel: LocationViewModel by viewModels {
        val repository = LocationRepository(apiService)
        LocationViewModelFactory(repository)
    }

    private var countryList: List<String> = emptyList()
    private var cityList: List<String> = emptyList()
    private var countryCodeMap: MutableMap<String, String> = mutableMapOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationSelectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("DEBUG", "Fragment açıldı, UI yüklendi.")

        binding.autoCompleteCountry.setDropDownBackgroundResource(R.drawable.bg_dropdown_soft)
        binding.autoCompleteCity.setDropDownBackgroundResource(R.drawable.bg_dropdown_soft)

        fetchCountries()
        observeViewModel()
        setupConfirmButton()
    }

    // 📌 **GeoNames API'den Ülkeleri Çek**
    private fun fetchCountries() {
        lifecycleScope.launch {
            Log.d("DEBUG", "fetchCountries() çağrıldı, GeoNames API'den veri çekiliyor...")
            viewModel.fetchCountries("cihan0203")
        }
    }

    private fun observeViewModel() {
        viewModel.countries.observe(viewLifecycleOwner) { countries ->
            countryList = countries.map { it.first }
            countryCodeMap = countries.toMap().toMutableMap()
            setUpCountryDropdown()
        }

        viewModel.cities.observe(viewLifecycleOwner) { cities ->
            cityList = cities
            setUpCityDropdown()
        }
    }

    private fun setUpCountryDropdown() {
        Log.d("DEBUG", "Ülke dropdown setup başladı: $countryList")

        val countryAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, countryList)
        binding.autoCompleteCountry.setAdapter(countryAdapter)

        binding.autoCompleteCountry.setOnItemClickListener { _, _, position, _ ->
            val selectedCountry = countryList[position]
            Log.d("DEBUG", "Seçilen Ülke: $selectedCountry")

            // **Ülke kodunu al**
            val countryCode = countryCodeMap[selectedCountry] ?: return@setOnItemClickListener

            // **Seçilen ülkeye göre şehirleri çek**
            fetchCities(countryCode)
        }

        // 📌 Dropdown açılmasını sağla
        binding.autoCompleteCountry.threshold = 1
        binding.autoCompleteCountry.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.autoCompleteCountry.showDropDown()
            }
        }
    }

    private fun fetchCities(countryCode: String) {
        lifecycleScope.launch {
            Log.d("DEBUG", "fetchCities() çağrıldı, ülke kodu: $countryCode")
            viewModel.fetchCitiesByCountry(countryCode, "cihan0203")
        }
    }

    private fun setUpCityDropdown() {
        Log.d("DEBUG", "Şehir dropdown setup başladı: $cityList")

        val cityAdapter = ArrayAdapter(requireContext(), android.R.layout.simple_dropdown_item_1line, cityList)
        binding.autoCompleteCity.setAdapter(cityAdapter)

        binding.autoCompleteCity.threshold = 1
        binding.autoCompleteCity.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                binding.autoCompleteCity.showDropDown()
            }
        }
    }

    private fun setupConfirmButton() {
        binding.btnConfirmSelection.setOnClickListener {
            val selectedCountry = binding.autoCompleteCountry.text.toString()
            val selectedCity = binding.autoCompleteCity.text.toString()

            if (selectedCountry.isEmpty() || selectedCity.isEmpty()) {
                Snackbar.make(binding.root, "Please select a country and city!", Snackbar.LENGTH_SHORT).show()
            } else {
                listener.onLocationSelected(selectedCountry, selectedCity)
                dismiss()
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}