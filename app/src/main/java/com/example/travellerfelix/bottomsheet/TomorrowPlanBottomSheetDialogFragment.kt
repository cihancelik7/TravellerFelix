package com.example.travellerfelix.bottomsheet

import android.os.Bundle
import android.view.*
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.travellerfelix.R
import com.example.travellerfelix.adapter.TomorrowPlanAdapter
import com.example.travellerfelix.databinding.BottomSheetTomorrowPlanBinding
import com.example.travellerfelix.viewmodel.TomorrowPlanViewModel
import com.example.travellerfelix.viewmodel.TomorrowPlanViewModelFactory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.travellerfelix.data.repository.TomorrowPlanRepository
import kotlinx.coroutines.launch

class TomorrowPlanBottomSheetDialogFragment(
    private val repository: TomorrowPlanRepository
) : DialogFragment() {

    private var _binding: BottomSheetTomorrowPlanBinding? = null
    private val binding get() = _binding!!

    private val viewModel: TomorrowPlanViewModel by viewModels {
        TomorrowPlanViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetTomorrowPlanBinding.inflate(inflater, container, false)

        // Dışarı tıklanırsa kapansın
        binding.rootLayout.setOnClickListener {
            dismiss()
        }

        // İçerideki CardView'e tıklanınca kapanmasın
        binding.rvPlans.setOnClickListener {} // Boş bırak

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.rvPlans.layoutManager = LinearLayoutManager(requireContext())

        viewModel.plans.observe(viewLifecycleOwner) {
            binding.rvPlans.adapter = TomorrowPlanAdapter(it)
        }

        lifecycleScope.launch {
            viewModel.fetchPlans()
        }

        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}