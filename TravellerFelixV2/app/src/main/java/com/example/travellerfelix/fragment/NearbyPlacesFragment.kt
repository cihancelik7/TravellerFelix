package com.example.travellerfelix.fragment

import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.travellerfelix.R
import com.example.travellerfelix.adapter.NearbyPlacesAdapter
import com.example.travellerfelix.data.remote.RetrofitClient
import com.example.travellerfelix.data.repository.NearbyRepository
import com.example.travellerfelix.viewmodel.NearbyViewModel
import com.example.travellerfelix.viewmodel.NearbyViewModelFactory
import com.example.travellerfelix.viewmodel.SharedLocationViewModel
import kotlinx.coroutines.flow.collectLatest
import java.util.Locale

class NearbyPlacesFragment : Fragment() {

    private lateinit var adapter: NearbyPlacesAdapter

    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var infoTextView: TextView
    private lateinit var currentLocationText: TextView
    private lateinit var btnFetch: Button

    private val viewModel: NearbyViewModel by viewModels {
        NearbyViewModelFactory(NearbyRepository(RetrofitClient.nearbyPlacesApiService))
    }
    private val sharedLocationViewModel: SharedLocationViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_nearby_places, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.nearbyRecyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        infoTextView = view.findViewById(R.id.infoTextView)
        currentLocationText = view.findViewById(R.id.currentLocationText)
        btnFetch = view.findViewById(R.id.btnFetchPlaces)

        adapter = NearbyPlacesAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        progressBar.visibility = View.GONE
        infoTextView.visibility = View.VISIBLE
        infoTextView.text = "Lütfen bir kategori seçiniz."

        btnFetch.setOnClickListener {
            fetchPlaces()
        }

        // 📍 Sayfa açıldığında current konumu dinle
        lifecycleScope.launchWhenStarted {
            sharedLocationViewModel.currentLocation.collectLatest { location ->
                if (location != null) {
                    val geocoder = Geocoder(requireContext(), Locale.getDefault())
                    val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)

                    if (!addresses.isNullOrEmpty()) {
                        val address = addresses[0]
                        val city = address.locality ?: address.subAdminArea ?: address.adminArea ?: "Bilinmeyen Şehir"
                        val country = address.countryName ?: "Bilinmeyen Ülke"
                        val text = "Konum: $city, $country"

                        currentLocationText.text = text
                        Log.d("NearbyFragment", "🏙️ Güncellenmiş konum: $text")
                    } else {
                        currentLocationText.text = "Konum: Bilinmiyor"
                    }
                } else {
                    currentLocationText.text = "Konum: Bilinmiyor"
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewModel.nearbyPlaces.collectLatest { responses ->
                val allPlaces = responses.flatMap { it.results }

                progressBar.visibility = View.GONE
                if (allPlaces.isEmpty()) {
                    infoTextView.visibility = View.VISIBLE
                    infoTextView.text = "Sonuç bulunamadı."
                } else {
                    infoTextView.visibility = View.GONE
                }
                adapter.submitList(allPlaces)
            }
        }
    }

    private fun fetchPlaces() {
        val types = getSelectedTypes()
        val radius = 2000

        if (types.isEmpty()) {
            progressBar.visibility = View.GONE
            recyclerView.visibility = View.GONE
            infoTextView.visibility = View.VISIBLE
            infoTextView.text = "Lütfen bir kategori seçiniz."
            return
        }

        sharedLocationViewModel.currentLocation.value?.let { location ->
            val locationString = "${location.latitude},${location.longitude}"
            Log.d("NearbyFragment", "📍 Liste çekiliyor: $locationString")
            progressBar.visibility = View.VISIBLE
            recyclerView.visibility = View.VISIBLE
            infoTextView.visibility = View.GONE

            viewModel.fetchNearbyPlaces(locationString, radius, types)
        } ?: run {
            Log.e("NearbyFragment", "⚠️ Konum bulunamadı, liste çekilemiyor")
            infoTextView.visibility = View.VISIBLE
            infoTextView.text = "Konum bulunamadı."
        }
    }

    private fun getSelectedTypes(): List<String> {
        val view = requireView()
        val selectedTypes = mutableListOf<String>()
        if (view.findViewById<CheckBox>(R.id.checkboxHotel).isChecked) selectedTypes.add("lodging")
        if (view.findViewById<CheckBox>(R.id.checkboxRestaurant).isChecked) selectedTypes.add("restaurant")
        if (view.findViewById<CheckBox>(R.id.checkboxMuseum).isChecked) selectedTypes.add("museum")
        if (view.findViewById<CheckBox>(R.id.checkboxTransport).isChecked) selectedTypes.add("transit_station")
        if (view.findViewById<CheckBox>(R.id.checkboxRent).isChecked) selectedTypes.add("car_rental")
        return selectedTypes
    }
}