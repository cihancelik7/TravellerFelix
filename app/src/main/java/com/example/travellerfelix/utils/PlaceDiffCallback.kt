package com.example.travellerfelix.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.travellerfelix.data.remote.model.PlaceResult

class PlaceDiffCallback: DiffUtil.ItemCallback<PlaceResult>() {
    override fun areItemsTheSame(oldItem: PlaceResult, newItem: PlaceResult): Boolean {
        return oldItem.placeId == newItem.placeId
    }

    override fun areContentsTheSame(oldItem: PlaceResult, newItem: PlaceResult): Boolean {
        return oldItem == newItem
    }
}