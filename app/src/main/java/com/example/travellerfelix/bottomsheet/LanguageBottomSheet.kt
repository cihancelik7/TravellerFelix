package com.example.travellerfelix.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import com.example.travellerfelix.R
import com.example.travellerfelix.utils.LanguageUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LanguageBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        val view = inflater.inflate(R.layout.bottom_sheet_language, container, false)
        val radioGroup = view.findViewById<RadioGroup>(R.id.languageRadioGroup)
        val applyButton = view.findViewById<Button>(R.id.applyLanguage)

        applyButton.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId != -1) {
                val selectedButton = view.findViewById<RadioButton>(selectedId)
                val languageCode = selectedButton.tag.toString()

                val prefs = requireContext().getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
                prefs.edit().putString("selected_language", languageCode).apply()

                LanguageUtil.setLocale(requireContext(), languageCode)
                requireActivity().recreate()
                dismiss()
            } else {
                Toast.makeText(requireContext(), getString(R.string.dil_seciniz), Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}