package com.example.travellerfelix.bottomsheet

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.travellerfelix.R
import com.example.travellerfelix.databinding.BottomSheetClearDataBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment


class ClearDataBottomSheet(
    private val onConfirmDelete: () -> Unit,
) : BottomSheetDialogFragment() {

    private var _binding: BottomSheetClearDataBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetClearDataBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.confirmText.text = "Tüm kayıtlı verileri silmek istediğinize emin misiniz?"

        binding.btnYes.setOnClickListener {
            onConfirmDelete()
            Toast.makeText(requireContext(), "Veriler Silindi", Toast.LENGTH_SHORT).show()
            dismiss()
            
        }

        binding.btnNo.setOnClickListener {
            dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}