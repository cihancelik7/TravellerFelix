package com.example.travellerfelix.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.core.text.HtmlCompat
import androidx.fragment.app.DialogFragment
import com.example.travellerfelix.R
import com.example.travellerfelix.databinding.DialogKvkkBinding
import com.example.travellerfelix.utils.PreferenceHelper
import com.google.android.material.shape.MaterialShapeDrawable

class KvkkDialogFragment(private val onConsentResult: (Boolean) -> Unit): DialogFragment() {

    private var _binding: DialogKvkkBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        _binding = DialogKvkkBinding.inflate(LayoutInflater.from(context))

        val dialog = Dialog(requireContext())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(binding.root)

        val radius = resources.getDimension(R.dimen.dialog_corner_radius)
        val bgColor = ContextCompat.getColor(requireContext(), R.color.softWhite)
        val borderColor = ContextCompat.getColor(requireContext(), R.color.textColor)

        val shapeDrawable = MaterialShapeDrawable().apply {
            fillColor = ColorStateList.valueOf(bgColor)
            strokeColor = ColorStateList.valueOf(borderColor)
            strokeWidth = 2f
            shapeAppearanceModel = shapeAppearanceModel.toBuilder()
                .setAllCornerSizes(radius)
                .build()
            elevation = 0f
        }

        dialog.window?.apply {
            setBackgroundDrawable(shapeDrawable)
            setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            setDimAmount(0.4f)
        }

        binding.kvkkContentText.text = getKvkkText()

        android.util.Log.d("CHECK_STRING", "devam string: ${getString(R.string.devam)}")

        binding.btnAccept.setOnClickListener {
            PreferenceHelper.setUserConsent(requireContext(), true)
            onConsentResult(true)
            dismiss()
        }

        binding.buttonDecline.setOnClickListener {
            PreferenceHelper.setUserConsent(requireContext(), false)
            onConsentResult(false)
            dismiss()
        }
        binding.kvkkContentText.text = HtmlCompat.fromHtml(
            getString(R.string.consent_kvkk_text),
            HtmlCompat.FROM_HTML_MODE_LEGACY
        )

        return dialog
    }


    private fun getKvkkText(): String {
        return """
        TravellerFelix olarak gizliliğinizi önemsiyoruz. Uygulamanın işlevselliğini artırmak amacıyla bazı kişisel verilere erişim izni talep etmekteyiz:

        • Konum Verisi: Seyahat planlarınızı daha iyi önermek ve yakındaki yerleri gösterebilmek için kullanılır.
        • Bildirim Erişimi: Hatırlatmalar, önemli uyarılar ve plan güncellemeleri gibi bilgileri size zamanında iletmek amacıyla kullanılır.

        Bu veriler yalnızca uygulama içinde, sizin deneyiminizi iyileştirmek için kullanılır. Üçüncü şahıslarla paylaşılmaz ve yalnızca açık rızanız doğrultusunda işlenir.

        Devam etmek için lütfen KVKK ve Gizlilik Politikasını okuduğunuzu ve kabul ettiğinizi onaylayın.
    """.trimIndent()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}