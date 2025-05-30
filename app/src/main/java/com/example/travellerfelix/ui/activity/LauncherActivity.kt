package com.example.travellerfelix.ui.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.travellerfelix.MainActivity
import com.example.travellerfelix.utils.PreferenceHelper

class LauncherActivity:AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent = if (PreferenceHelper.isFirstTime(this)){
            Intent(this,OnboardingActivity::class.java)
        }else{
            Intent(this,MainActivity::class.java)
        }
        startActivity(intent)
        finish()
    }
}