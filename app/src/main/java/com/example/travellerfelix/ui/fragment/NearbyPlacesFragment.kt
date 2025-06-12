package com.example.travellerfelix.ui.fragment

import android.location.Geocoder
import android.os.Bundle
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
import java.util.*

class NearbyPlacesFragment : Fragment() {

    private lateinit var adapter: NearbyPlacesAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var progressBar: ProgressBar
    private lateinit var infoTextView: TextView
    private lateinit var currentLocationText: TextView
    private lateinit var btnFetch: Button
    private lateinit var btnLoadMore: Button

    private val viewModel: NearbyViewModel by viewModels {
        NearbyViewModelFactory(NearbyRepository(RetrofitClient.nearbyPlacesApiService))
    }
    private val sharedLocationViewModel: SharedLocationViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_nearby_places, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.nearbyRecyclerView)
        progressBar = view.findViewById(R.id.progressBar)
        infoTextView = view.findViewById(R.id.infoTextView)
        currentLocationText = view.findViewById(R.id.currentLocationText)
        btnFetch = view.findViewById(R.id.btnFetchPlaces)
        btnLoadMore = view.findViewById(R.id.btnLoadMore)

        adapter = NearbyPlacesAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        infoTextView.text = "Lütfen bir kategori seçiniz."
        progressBar.visibility = View.GONE

        btnFetch.setOnClickListener {
            fetchPlaces()
        }

        btnLoadMore.setOnClickListener {
            viewModel.loadNextPage()
        }

        observeLocationUpdates()
        observeVisiblePlaces()
    }

    private fun observeLocationUpdates() {
        lifecycleScope.launchWhenStarted {
            sharedLocationViewModel.currentLocation.collectLatest { location ->
                location?.let {
                    val addresses = Geocoder(requireContext(), Locale.getDefault())
                        .getFromLocation(it.latitude, it.longitude, 1)
                    currentLocationText.text = if (!addresses.isNullOrEmpty()) {
                        val city = addresses[0].locality ?: "Bilinmeyen Şehir"
                        val country = addresses[0].countryName ?: "Bilinmeyen Ülke"
                        "Konum: $city, $country"
                    } else "Konum: Bilinmiyor"
                } ?: run {
                    currentLocationText.text = "Konum: Bilinmiyor"
                }
            }
        }
    }

    private fun observeVisiblePlaces() {
        lifecycleScope.launchWhenStarted {
            viewModel.visiblePlaces.collectLatest { visible ->
                progressBar.visibility = View.GONE
                if (visible.isEmpty()) {
                    infoTextView.visibility = View.VISIBLE
                    infoTextView.text = "Sonuç bulunamadı."
                    btnLoadMore.visibility = View.GONE
                } else {
                    infoTextView.visibility = View.GONE
                    adapter.submitList(visible)
                    val hasMore = visible.size < viewModel.totalCount()
                    btnLoadMore.visibility = if (hasMore) View.VISIBLE else View.GONE
                }
            }
        }
    }

    private fun fetchPlaces() {
        val types = getSelectedTypes()
        if (types.isEmpty()) {
            infoTextView.visibility = View.VISIBLE
            infoTextView.text = "Lütfen bir kategori seçiniz."
            return
        }

        sharedLocationViewModel.currentLocation.value?.let { location ->
            val locationString = "${location.latitude},${location.longitude}"
            progressBar.visibility = View.VISIBLE
            infoTextView.visibility = View.GONE
            viewModel.fetchNearbyPlaces(locationString, 2000, types)
        } ?: run {
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