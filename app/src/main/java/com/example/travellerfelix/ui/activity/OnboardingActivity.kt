package com.example.travellerfelix.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.travellerfelix.MainActivity
import com.example.travellerfelix.R
import com.example.travellerfelix.adapter.OnboardingAdapter
import com.example.travellerfelix.data.local.model.OnboardingItem
import com.example.travellerfelix.databinding.ActivityOnboardingBinding
import com.example.travellerfelix.utils.PreferenceHelper

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingAdapter: OnboardingAdapter
    private lateinit var dots: Array<TextView?>
    private var currentPage = 0
    private val LOCATION_PERMISSION_REQUEST_CODE = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOnboardingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupOnboardingItems()
        setupDotsIndicator(0)

        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                currentPage = position
                setupDotsIndicator(position)
                updateButtonText(position)
                binding.consentLayout.visibility = if (position == onboardingAdapter.itemCount - 1) View.VISIBLE else View.GONE
            }
        })

        binding.nextButton.setOnClickListener {
            if (currentPage < onboardingAdapter.itemCount - 1) {
                binding.viewPager.currentItem = currentPage + 1
            } else {
                if (!binding.checkboxConsent.isChecked) {
                    Toast.makeText(this, "Lütfen kullanıcı sözleşmesini kabul edin.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                showConsentDialog()
            }
        }

        binding.textConsentLink.setOnClickListener {
            showConsentDialog()
        }
    }

    private fun setupOnboardingItems() {
        val items = listOf(
            OnboardingItem(
                title = getString(R.string.onboarding_title_1),
                description = getString(R.string.onboarding_desc_1),
                imageResId = R.drawable.calendar_gpt
            ),
            OnboardingItem(
                title = getString(R.string.onboarding_title_2),
                description = getString(R.string.onboarding_desc_2),
                imageResId = R.drawable.nearby_gpt
            ),
            OnboardingItem(
                title = getString(R.string.onboarding_title_3),
                description = getString(R.string.onboarding_desc_3),
                imageResId = R.drawable.trip
            )
        )
        onboardingAdapter = OnboardingAdapter(items)
        binding.viewPager.adapter = onboardingAdapter
    }

    private fun setupDotsIndicator(position: Int) {
        binding.dotsLayout.removeAllViews()
        dots = arrayOfNulls(onboardingAdapter.itemCount)

        for (i in dots.indices) {
            dots[i] = TextView(this).apply {
                text = "●"
                textSize = 12f
                setTextColor(
                    ContextCompat.getColor(
                        this@OnboardingActivity,
                        if (i == position) R.color.primaryColor else R.color.textColor
                    )
                )
                binding.dotsLayout.addView(this)
            }
        }
    }

    private fun updateButtonText(position: Int) {
        binding.nextButton.text = if (position == onboardingAdapter.itemCount - 1)
            getString(R.string.basla) else getString(R.string.devam)
    }

    private fun showConsentDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_kvkk, null)
        val dialog = android.app.AlertDialog.Builder(this)
            .setView(dialogView)
            .setCancelable(false)
            .create()

        val kvkkTextView = dialogView.findViewById<TextView>(R.id.kvkkContentText)
        kvkkTextView.text = getString(R.string.consent_kvkk_text)

        dialogView.findViewById<TextView>(R.id.btnAccept).setOnClickListener {
            PreferenceHelper.setUserConsent(this, true)
            PreferenceHelper.setNoFirstTime(this)
            dialog.dismiss()
            requestLocationPermission()
        }

        dialogView.findViewById<TextView>(R.id.buttonDecline).setOnClickListener {
            PreferenceHelper.setUserConsent(this, false)
            PreferenceHelper.setNoFirstTime(this)
            dialog.dismiss()
            goToMain()
        }

        dialog.show()
    }

    private fun requestLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            goToMain()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            goToMain() // kullanıcı izin verse de vermese de geçiş yapılır, kontrol MainActivity'de
        }
    }

    private fun goToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}