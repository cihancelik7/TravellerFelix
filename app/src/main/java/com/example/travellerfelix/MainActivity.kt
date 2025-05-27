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
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.travellerfelix.data.local.model.DeviceLocation
import com.example.travellerfelix.databinding.ActivityMainBinding
import com.example.travellerfelix.utils.LanguageUtil
import com.example.travellerfelix.utils.PermissionManager
import com.example.travellerfelix.utils.PreferenceHelper
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

    private lateinit var permissionSettingsLauncher: ActivityResultLauncher<Intent>
    private var returnedFromSettings = false

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

        // Navigation
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)

        sharedLocationViewModel = ViewModelProvider(this)[SharedLocationViewModel::class.java]
        sharedCalendarViewModel = ViewModelProvider(this)[SharedCalendarViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // 🔁 Ayarlardan dönüldüğünü algıla
        permissionSettingsLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            returnedFromSettings = true
        }

        // BottomBar izin kontrolü
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.nearbyFragment) {
                if (!PreferenceHelper.hasUserGivenConsent(this) || !hasLocationPermission()) {
                    showPermissionSettingsDialog()
                    return@setOnItemSelectedListener false
                }
            }
            navController.navigate(item.itemId)
            true
        }

        // Uygulama ilk açıldığında izin varsa başlat
        if (PreferenceHelper.hasUserGivenConsent(this) && hasLocationPermission()) {
            startLocationUpdates()
        }
    }

    override fun onResume() {
        super.onResume()

        if (returnedFromSettings) {
            returnedFromSettings = false
            Log.d("MainActivity", "🔁 Ayarlardan dönüldü, yeniden izin kontrolü yapılıyor")

            if (PreferenceHelper.hasUserGivenConsent(this) && hasLocationPermission()) {
                Log.d("MainActivity", "✅ İzin mevcut, konum başlatılıyor.")
                startLocationUpdates()
            } else {
                Log.d("MainActivity", "❌ Hâlâ izin yok.")
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        if (!hasLocationPermission()) return

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
        return (ContextCompat.checkSelfPermission(this, fine) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, coarse) == PackageManager.PERMISSION_GRANTED)
    }

    private fun showPermissionSettingsDialog() {
        androidx.appcompat.app.AlertDialog.Builder(this)
            .setTitle("İzin Gerekli")
            .setMessage("Yakındaki yerleri görebilmek için konum iznine ihtiyacımız var. Lütfen uygulama ayarlarından izni etkinleştirin.")
            .setPositiveButton("Ayarları Aç") { _, _ ->
                val intent = Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                    data = Uri.fromParts("package", packageName, null)
                }
                permissionSettingsLauncher.launch(intent)
            }
            .setNegativeButton("Vazgeç", null)
            .show()
    }
}