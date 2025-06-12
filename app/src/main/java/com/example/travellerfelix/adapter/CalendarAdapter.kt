package com.example.travellerfelix.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.travellerfelix.R
import com.example.travellerfelix.data.local.model.DayItem
import java.text.SimpleDateFormat
import java.util.*

class CalendarAdapter : ListAdapter<DayItem, CalendarAdapter.CalendarViewHolder>(
    object : DiffUtil.ItemCallback<DayItem>() {
        override fun areItemsTheSame(oldItem: DayItem, newItem: DayItem): Boolean {
            return oldItem.date == newItem.date
        }

        override fun areContentsTheSame(oldItem: DayItem, newItem: DayItem): Boolean {
            return oldItem.date == newItem.date &&
                    oldItem.dayName == newItem.dayName &&
                    oldItem.isAvailable == newItem.isAvailable
        }
    }
) {
    private var onDateClickListener: ((String) -> Unit)? = null
    private var selectedDate: String? = null

    class CalendarViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val dayName: TextView = view.findViewById(R.id.date_item_day_name)
        private val dayNumber: TextView = view.findViewById(R.id.date_item_day_number)
        private val availableDot: View = view.findViewById(R.id.date_item_available)
        private val dayLayout: LinearLayout = view.findViewById(R.id.day_item_layout)

        fun bind(item: DayItem, isSelected: Boolean, onDateClickListener: ((String) -> Unit)?) {
            dayName.text = item.dayName
            val dateParts = item.date.split("-")
            dayNumber.text = if (dateParts.size == 3) dateParts[2] else "??"

            when {
                isSelected -> {
                    dayLayout.setBackgroundColor(Color.parseColor("#A5D6A7")) // Seçili yeşil ton
                    dayNumber.setTextColor(Color.WHITE)
                    dayName.setTextColor(Color.WHITE)
                }
                item.date == getTodayDate() -> {
                    dayLayout.setBackgroundColor(Color.parseColor("#FFFFFF")) // Bugün rengi
                    dayNumber.setTextColor(Color.parseColor("#006400"))
                    dayName.setTextColor(Color.parseColor("#006400"))
                }
                else -> {
                    dayLayout.setBackgroundResource(R.drawable.date_item_default_background) // 💡 Burada border'lı
                    dayNumber.setTextColor(Color.LTGRAY)
                    dayName.setTextColor(Color.LTGRAY)
                }
            }

            availableDot.visibility = if (item.isAvailable) View.VISIBLE else View.INVISIBLE

            itemView.setOnClickListener {
                onDateClickListener?.invoke(item.date)
            }
        }

        private fun getTodayDate(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return sdf.format(Date())
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.date_item, parent, false)
        return CalendarViewHolder(view)
    }

    override fun onBindViewHolder(holder: CalendarViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, item.date == selectedDate, onDateClickListener)
    }

    fun setOnDateClickListener(listener: (String) -> Unit) {
        onDateClickListener = { date ->
            selectedDate = date
            notifyDataSetChanged()
            listener(date)
        }
    }
    fun forceRedraw() {
        notifyItemRangeChanged(0, itemCount)
    }
    fun forceRefresh() {
        val currentList = currentList.toList() // immutability sağla
        submitList(emptyList()) // önce temizle
        submitList(currentList) // tekrar gönder
    }

    fun getSelectedDate(): String? = selectedDate
}
