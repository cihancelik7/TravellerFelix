package com.example.travellerfelix.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.travellerfelix.BuildConfig
import com.example.travellerfelix.databinding.BottomSheetAboutBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class AboutBottomSheet : BottomSheetDialogFragment(){
    private var _binding: BottomSheetAboutBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAboutBinding.inflate(inflater,container,false)

        binding.appVersion.text ="Version: "+ BuildConfig.VERSION_NAME

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}