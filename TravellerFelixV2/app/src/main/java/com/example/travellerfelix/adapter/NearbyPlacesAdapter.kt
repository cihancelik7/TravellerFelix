package com.example.travellerfelix.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.travellerfelix.data.remote.model.PlaceResult
import com.example.travellerfelix.databinding.ItemNearbyPlaceBinding
import com.example.travellerfelix.utils.PlaceDiffCallback

class NearbyPlacesAdapter : ListAdapter<PlaceResult, NearbyPlacesAdapter.NearbyPlaceViewHolder>(PlaceDiffCallback()) {

    inner class NearbyPlaceViewHolder(private val binding: ItemNearbyPlaceBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(place: PlaceResult) {
            Log.d("NearbyPlacesAdapter", "Veri Bağlanıyor: ${place.name}, ${place.vicinity}, ${place.rating}")
            binding.placeName.text = place.name ?: "Unknown"
            binding.placeAddress.text = place.vicinity ?: "No address"
            binding.placeRating.text = "⭐ ${place.rating?.toString() ?: "N/A"} (${place.userRatingsTotal ?: 0} yorum)"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NearbyPlaceViewHolder {
        val binding = ItemNearbyPlaceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return NearbyPlaceViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NearbyPlaceViewHolder, position: Int) {
        holder.bind(currentList[position])
    }
}