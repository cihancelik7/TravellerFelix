package com.example.travellerfelix.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travellerfelix.R
import com.example.travellerfelix.data.local.model.OnboardingItem
import com.example.travellerfelix.databinding.ItemOnboardingPageBinding

class OnboardingAdapter(private val onBoardingItems: List<OnboardingItem>):
RecyclerView.Adapter<OnboardingAdapter.OnboardingViewHolder>(){

    inner class OnboardingViewHolder(val binding: ItemOnboardingPageBinding):
            RecyclerView.ViewHolder(binding.root){

                fun bind(item: OnboardingItem){
                    binding.onboardingTitle.text = item.title
                    binding.onboardingDescription.text = item.description
                    binding.onboardingImage.setImageResource(item.imageResId)
                }
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OnboardingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_onboarding_page,parent,false)
        return OnboardingViewHolder(ItemOnboardingPageBinding.bind(view))
    }

    override fun getItemCount(): Int = onBoardingItems.size

    override fun onBindViewHolder(holder: OnboardingViewHolder, position: Int) {
        holder.bind(onBoardingItems[position])
    }


}