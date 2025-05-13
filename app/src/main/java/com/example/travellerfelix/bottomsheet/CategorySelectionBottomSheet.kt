package com.example.travellerfelix.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import com.example.travellerfelix.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class CategorySelectionBottomSheet(
    private val listener: OnCategorySelectedListener,
    private val selectedDate: String
) : BottomSheetDialogFragment() {

    interface OnCategorySelectedListener {
        fun onCategorySelected(category: String, selectedDate: String)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.bottom_sheet_add_plan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoryRadioGroup = view.findViewById<RadioGroup>(R.id.categoryRadioGroup)
        val nextBtn = view.findViewById<Button>(R.id.nextButton)
        val cancelBtn = view.findViewById<Button>(R.id.cancelButton)

        nextBtn.setOnClickListener {
            val selectedRadioButtonId = categoryRadioGroup.checkedRadioButtonId
            if (selectedRadioButtonId != -1) {
                val selectedRadioButton = view.findViewById<RadioButton>(selectedRadioButtonId)
                val selectedCategory = selectedRadioButton.tag.toString()  // 👈 Önemli Değişiklik
                listener.onCategorySelected(selectedCategory, selectedDate)
                dismiss()
            }
        }

        cancelBtn.setOnClickListener { dismiss() }
    }
}