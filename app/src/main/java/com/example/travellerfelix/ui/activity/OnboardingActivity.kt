package com.example.travellerfelix.ui.activity

import android.content.Intent
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.travellerfelix.MainActivity
import com.example.travellerfelix.R
import com.example.travellerfelix.adapter.OnboardingAdapter
import com.example.travellerfelix.data.local.model.OnboardingItem
import com.example.travellerfelix.databinding.ActivityOnboardingBinding

class OnboardingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityOnboardingBinding
    private lateinit var onboardingAdapter: OnboardingAdapter
    private lateinit var dots: Array<TextView?>
    private var currentPage = 0

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
            }
        })

        binding.nextButton.setOnClickListener {
            if (currentPage < onboardingAdapter.itemCount - 1) {
                binding.viewPager.currentItem = currentPage + 1
            } else {
                // TODO: Onboarding bittiğinde yapılacak işlem (örneğin MainActivity'e geçiş)
                 startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
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
}