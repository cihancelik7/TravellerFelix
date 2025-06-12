package com.example.travellerfelix.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.travellerfelix.data.local.model.TomorrowPlanItem
import com.example.travellerfelix.databinding.ItemTomorrowPlanBinding

class TomorrowPlanAdapter(private val plans: List<TomorrowPlanItem>) :
    RecyclerView.Adapter<TomorrowPlanAdapter.PlanViewHolder>() {

    inner class PlanViewHolder(private val binding: ItemTomorrowPlanBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(plan: TomorrowPlanItem) {
            binding.tvCity.text = plan.city
            binding.tvCategory.text = plan.category
            binding.tvTimeContent.text = "${plan.time} - ${plan.content}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val binding =
            ItemTomorrowPlanBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlanViewHolder(binding)
    }

    override fun getItemCount(): Int =plans.size

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        holder.bind(plans[position])
    }


}