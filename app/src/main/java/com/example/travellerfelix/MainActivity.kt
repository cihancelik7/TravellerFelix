package com.example.travellerfelix

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.travellerfelix.data.local.model.DeviceLocation
import com.example.travellerfelix.databinding.ActivityMainBinding
import com.example.travellerfelix.utils.LanguageUtil
import com.example.travellerfelix.viewmodel.SharedCalendarViewModel
import com.example.travellerfelix.viewmodel.SharedLocationViewModel
import com.google.android.gms.location.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private lateinit var sharedLocationViewModel: SharedLocationViewModel
    private lateinit var sharedCalendarViewModel: SharedCalendarViewModel

    override fun attachBaseContext(newBase: Context) {
        val sharedPrefs = newBase.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
        val langCode = sharedPrefs.getString("selected_language", "tr") ?: "tr"
        val context = LanguageUtil.setLocale(newBase, langCode)
        super.attachBaseContext(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        sharedLocationViewModel = ViewModelProvider(this)[SharedLocationViewModel::class.java]
        sharedCalendarViewModel = ViewModelProvider(this)[SharedCalendarViewModel::class.java]

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (hasLocationPermission()) {
            startLocationUpdates()
        } else {
            requestLocationPermission()
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setMinUpdateIntervalMillis(5000)
            .build()

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                val lastLocation = result.lastLocation
                if (lastLocation != null) {
                    val deviceLocation = DeviceLocation(
                        latitude = lastLocation.latitude,
                        longitude = lastLocation.longitude
                    )
                    val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                    sharedLocationViewModel.updateLocation(deviceLocation, geocoder)
                    Log.d("MainActivity", "📍 Konum güncellendi: ${deviceLocation.latitude}, ${deviceLocation.longitude}")
                } else {
                    Log.e("MainActivity", "⚠️ Konum alınamadı")
                }
                fusedLocationClient.removeLocationUpdates(this)
            }
        }

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null)
    }

    private fun hasLocationPermission(): Boolean {
        val fine = Manifest.permission.ACCESS_FINE_LOCATION
        val coarse = Manifest.permission.ACCESS_COARSE_LOCATION
        return (ContextCompat.checkSelfPermission(this, fine) == android.content.pm.PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, coarse) == android.content.pm.PackageManager.PERMISSION_GRANTED)
    }

    private fun requestLocationPermission() {
        requestPermissions(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            1001
        )
    }
}