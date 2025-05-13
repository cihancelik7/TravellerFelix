package com.example.travellerfelix.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.example.travellerfelix.databinding.BottomSheetFeedbackBinding
import com.example.travellerfelix.viewmodel.SharedLocationViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FeedbackBottomSheet : BottomSheetDialogFragment() {

    private var _binding: BottomSheetFeedbackBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedLocationViewModel: SharedLocationViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetFeedbackBinding.inflate(inflater, container, false)
        sharedLocationViewModel = ViewModelProvider(requireActivity())[SharedLocationViewModel::class.java]

        binding.submitButton.setOnClickListener {
            val message = binding.feedbackMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                val lat = sharedLocationViewModel.currentLocation.value?.latitude
                val lon = sharedLocationViewModel.currentLocation.value?.longitude
                val country = sharedLocationViewModel.country.value
                val city = sharedLocationViewModel.city.value

                saveFeedbackToFirebase(message, lat, lon, country, city)
            } else {
                Toast.makeText(requireContext(), "Lütfen bir mesaj girin..", Toast.LENGTH_SHORT).show()
            }
        }

        return binding.root
    }

    private fun saveFeedbackToFirebase(message: String, lat: Double?, lon: Double?, country: String, city: String) {
        val feedbackMap = mutableMapOf<String, Any>(
            "country" to country,
            "city" to city,
            "timestamp" to getCurrentTime(),
            "message" to message
        )

        if (lat != null && lon != null) {
            feedbackMap["latitude"] = lat
            feedbackMap["longitude"] = lon
        }

        val databaseRef = FirebaseDatabase.getInstance().getReference("feedbacks")
        val newRef = databaseRef.push()
        newRef.setValue(feedbackMap)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Geri bildiriminiz alındı!", Toast.LENGTH_SHORT).show()
                dismiss()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Hata Oluştu, Tekrar Deneyin!", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getCurrentTime(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        return formatter.format(Date())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}