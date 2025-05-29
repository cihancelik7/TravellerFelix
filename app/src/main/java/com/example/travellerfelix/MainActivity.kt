package com.example.travellerfelix

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.travellerfelix.data.local.model.DeviceLocation
import com.example.travellerfelix.databinding.ActivityMainBinding
import com.example.travellerfelix.ui.fragment.LocationPermissionDialogFragment
import com.example.travellerfelix.utils.LanguageUtil
import com.example.travellerfelix.utils.PreferenceHelper
import com.example.travellerfelix.viewmodel.SharedCalendarViewModel
import com.example.travellerfelix.viewmodel.SharedLocationViewModel
import com.google.android.gms.location.*
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var locationCallback: LocationCallback
    private lateinit var permissionLauncher: ActivityResultLauncher<Array<String>>

    private lateinit var sharedLocationViewModel: SharedLocationViewModel
    private lateinit var sharedCalendarViewModel: SharedCalendarViewModel

    private var isPermissionDialogShowing = false

    override fun attachBaseContext(newBase: Context) {
        val langCode = newBase.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
            .getString("selected_language", "tr") ?: "tr"
        super.attachBaseContext(LanguageUtil.setLocale(newBase, langCode))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.bottomNavigation.setupWithNavController(navHostFragment.navController)

        sharedLocationViewModel = ViewModelProvider(this)[SharedLocationViewModel::class.java]
        sharedCalendarViewModel = ViewModelProvider(this)[SharedCalendarViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupLocationCallback()
        setupPermissionLauncher()

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.nearbyFragment) {
                if (!PreferenceHelper.hasUserGivenConsent(this) || !hasLocationPermission()) {
                    if (PreferenceHelper.isLocationPermissionDeferred(this)) {
                        val dialog = LocationPermissionDialogFragment{
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION
                                )
                            )
                        }
                        dialog.show(supportFragmentManager,"LocationPermissionDialog")
                        return@setOnItemSelectedListener false
                    }
                    requestLocationPermission()
                    return@setOnItemSelectedListener false
                }
            }
            navHostFragment.navController.navigate(item.itemId)
            true
        }

        if (PreferenceHelper.hasUserGivenConsent(this) && hasLocationPermission()) {
            startLocationUpdates()
        }
    }

    override fun onResume() {
        super.onResume()
        if (hasLocationPermission()) {
            startLocationUpdates()
        }
    }

    private fun setupPermissionLauncher() {
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                    permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            if (granted) {
                PreferenceHelper.clearDeferredLocationPermission(this)
                startLocationUpdates()
            } else {
                showPermissionSettingsDialog()
            }
        }
    }

    private fun hasLocationPermission(): Boolean {
        val fine = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fine == PackageManager.PERMISSION_GRANTED && coarse == PackageManager.PERMISSION_GRANTED
    }

    private fun requestLocationPermission() {
        if (!isPermissionDialogShowing) {
            isPermissionDialogShowing = true

            val dialog = LocationPermissionDialogFragment {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
            dialog.show(supportFragmentManager, "LocationPermissionDialog")
        }
    }

    private fun showPermissionSettingsDialog() {
        AlertDialog.Builder(this)
            .setTitle("İzin Gerekli")
            .setMessage("Bu özelliği kullanabilmek için konum izni gereklidir. Lütfen ayarlardan izin verin.")
            .setPositiveButton("Ayarları Aç") { _, _ ->
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
                startActivity(intent)
            }
            .setNegativeButton("Vazgeç", null)
            .show()
    }

    private fun setupLocationCallback() {
        locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    val deviceLocation = DeviceLocation(location.latitude, location.longitude)
                    val geocoder = Geocoder(this@MainActivity, Locale.getDefault())
                    sharedLocationViewModel.updateLocation(deviceLocation, geocoder)
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10000)
            .setMinUpdateIntervalMillis(5000)
            .build()
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                location?.let {
                    val loc = DeviceLocation(it.latitude, it.longitude)
                    sharedLocationViewModel.updateLocation(loc, Geocoder(this, Locale.getDefault()))
                }
            }
    }

}