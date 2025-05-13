package com.example.travellerfelix.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travellerfelix.R
import com.example.travellerfelix.data.local.model.*
import com.example.travellerfelix.databinding.*

class DayPlanAdapter (private val onItemClick:(CityCountryProvider) -> Unit
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val allPlansList = mutableListOf<Any>()

    fun submitList(allDayPlans: AllDayPlans) {
        allPlansList.clear()
        allPlansList.apply {
            addAll(allDayPlans.hotels)
            addAll(allDayPlans.restaurants)
            addAll(allDayPlans.transports)
            addAll(allDayPlans.rents)
            addAll(allDayPlans.museums)
        }
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = allPlansList.size

    override fun getItemViewType(position: Int): Int {
        return when (allPlansList[position]) {
            is Hotel -> R.layout.item_hotel
            is Restaurant -> R.layout.item_restaurant
            is Transport -> R.layout.item_transportation
            is Rent -> R.layout.item_rent
            is Museum -> R.layout.item_museum
            else -> throw IllegalArgumentException("Bilinmeyen ViewType!")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            R.layout.item_hotel -> HotelViewHolder(ItemHotelBinding.inflate(inflater, parent, false),onItemClick)
            R.layout.item_restaurant -> RestaurantViewHolder(ItemRestaurantBinding.inflate(inflater, parent, false),onItemClick)
            R.layout.item_transportation -> TransportViewHolder(ItemTransportationBinding.inflate(inflater, parent, false),onItemClick)
            R.layout.item_rent -> RentViewHolder(ItemRentBinding.inflate(inflater, parent, false),onItemClick)
            R.layout.item_museum -> MuseumViewHolder(ItemMuseumBinding.inflate(inflater, parent, false),onItemClick)
            else -> throw IllegalArgumentException("Bilinmeyen ViewHolder!")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (val item = allPlansList[position]) {
            is Hotel -> (holder as HotelViewHolder).bind(item)
            is Restaurant -> (holder as RestaurantViewHolder).bind(item)
            is Transport -> (holder as TransportViewHolder).bind(item)
            is Rent -> (holder as RentViewHolder).bind(item)
            is Museum -> (holder as MuseumViewHolder).bind(item)
        }
    }

    class HotelViewHolder(private val binding: ItemHotelBinding,
        private val onItemClick: (CityCountryProvider) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(hotel: Hotel) {
            binding.placeName2.text = hotel.name
            binding.editTextDate3.setText(hotel.checkInDate)
            binding.editTextDate4.setText(hotel.checkOutDate)
            binding.pickUpTime.text = hotel.checkInTime

            itemView.setOnClickListener{
                onItemClick(hotel)
            }
        }
    }

    class RestaurantViewHolder(private val binding: ItemRestaurantBinding,
        private val onItemClick: (CityCountryProvider) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(restaurant: Restaurant) {
            binding.placeName2.text = restaurant.name
            binding.reservationTime.text = restaurant.reservationTime
            binding.openCloseTime2.text = restaurant.openCloseTime

            itemView.setOnClickListener{
                onItemClick(restaurant)
            }
        }
    }

    class TransportViewHolder(private val binding: ItemTransportationBinding,
        private val onItemClick: (CityCountryProvider) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(transport: Transport) {
            binding.placeName2.text = transport.ticketName
            binding.textView2.text = transport.departureTime
            binding.pickUpTime.text = transport.reservationTime

            itemView.setOnClickListener {
                onItemClick(transport)
            }
        }
    }

    class RentViewHolder(private val binding: ItemRentBinding,
        private val onItemClick: (CityCountryProvider) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(rent: Rent) {
            binding.placeName2.text = rent.rentalCompany
            binding.pickUpTime.text = rent.pickUpTime
            binding.dropoffTime.text = rent.dropOffTime
            binding.editTextDate.setText(rent.pickupDate)
            binding.editTextDate2.setText(rent.dropOffDate)
            binding.textView9.text = rent.pickupLocation
            binding.textView11.text = rent.dropOffLocation

            itemView.setOnClickListener {
                onItemClick(rent)
            }
        }
    }

    class MuseumViewHolder(private val binding: ItemMuseumBinding,
        private val onItemClick: (CityCountryProvider) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(museum: Museum) {
            binding.placeName2.text = museum.name
            binding.pickUpTime.text = museum.visitTime
            binding.openCloseTime2.text = museum.openCloseTime

            itemView.setOnClickListener {
                onItemClick(museum)
            }
        }
    }
}