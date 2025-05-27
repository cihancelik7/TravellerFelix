package com.example.travellerfelix.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import com.example.travellerfelix.databinding.DialogKvkkBinding
import com.example.travellerfelix.utils.PreferenceHelper

class KvkkDialogFragment(private val onConsentResult: (Boolean) -> Unit): DialogFragment() {

    private var _binding: DialogKvkkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogKvkkBinding.inflate(LayoutInflater.from(context))

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(binding.root)

        binding.kvkkContentText.text = getKvkkText()

        binding.btnAccept.setOnClickListener {
            PreferenceHelper.setUserConsent(requireContext(),true)
            onConsentResult(true)
            dismiss()
        }
        binding.buttonDecline.setOnClickListener {
            PreferenceHelper.setUserConsent(requireContext(),false)
            onConsentResult(false)
            dismiss()
        }
        return builder.create()
    }

    private fun getKvkkText(): String {
        return """
            TravellerFelix, gizliliğinizi önemser. Uygulamayı kullanırken konum verileriniz sadece öneri ve planlama amacıyla kullanılacaktır.
            Kişisel verileriniz 3. şahıslarla paylaşılmaz. Uygulama içi bazı özelliklerin çalışabilmesi için KVKK kapsamında onayınıza ihtiyaç duyulmaktadır.
            
            Devam etmek için lütfen KVKK ve gizlilik politikasını kabul edin.
            """.trimIndent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}