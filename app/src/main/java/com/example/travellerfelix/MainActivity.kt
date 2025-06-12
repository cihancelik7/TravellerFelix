package com.example.travellerfelix

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.example.travellerfelix.bottomsheet.TomorrowPlanBottomSheetDialogFragment
import com.example.travellerfelix.data.local.database.TravelDatabase
import com.example.travellerfelix.data.local.model.DeviceLocation
import com.example.travellerfelix.data.repository.TomorrowPlanRepository
import com.example.travellerfelix.databinding.ActivityMainBinding
import com.example.travellerfelix.service.TomorrowPlanAlarmReceiver
import com.example.travellerfelix.service.TomorrowPlanNotificationScheduler
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
    private lateinit var notificationPermissionLauncher: ActivityResultLauncher<String>

    private lateinit var sharedLocationViewModel: SharedLocationViewModel
    private lateinit var sharedCalendarViewModel: SharedCalendarViewModel

    private var isPermissionDialogShowing = false

    override fun attachBaseContext(newBase: Context) {
        val langCode = newBase.getSharedPreferences("language_prefs", Context.MODE_PRIVATE)
            .getString("selected_language", "tr") ?: "tr"
        super.attachBaseContext(LanguageUtil.setLocale(newBase, langCode))
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        requestedOrientation = android.content.pm.ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = android.graphics.Color.TRANSPARENT
        }


        // ViewModel'lar
        sharedLocationViewModel = ViewModelProvider(this)[SharedLocationViewModel::class.java]
        sharedCalendarViewModel = ViewModelProvider(this)[SharedCalendarViewModel::class.java]
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        setupLocationCallback()
        setupPermissionLauncher()
        createNotificationChannelIfNeeded()
        // Bildirim izni kontrolü
        setupNotificationPermissionLauncher()
        checkAndRequestNotificationPermission()

        // Navigation
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        binding.bottomNavigation.setupWithNavController(navHostFragment.navController)

        binding.bottomNavigation.setOnItemSelectedListener { item ->
            if (item.itemId == R.id.nearbyFragment) {
                if (!PreferenceHelper.hasUserGivenConsent(this) || !hasLocationPermission()) {
                    if (PreferenceHelper.isLocationPermissionDeferred(this)) {
                        val dialog = LocationPermissionDialogFragment {
                            permissionLauncher.launch(
                                arrayOf(
                                    Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION,
                                    Manifest.permission.POST_NOTIFICATIONS
                                )
                            )
                        }
                        dialog.show(supportFragmentManager, "LocationPermissionDialog")
                        return@setOnItemSelectedListener false
                    }
                    requestLocationPermission()
                    return@setOnItemSelectedListener false
                }
            }
            navHostFragment.navController.navigate(item.itemId)
            true
        }

        // Konum güncelleme
        if (PreferenceHelper.hasUserGivenConsent(this) && hasLocationPermission()) {
            startLocationUpdates()
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                AlertDialog.Builder(this)
                    .setTitle("Zamanlı Bildirim İzni Gerekli")
                    .setMessage("Yarınki planlarınız için bildirim alabilmek adına sistem ayarlarında 'Kesin Alarmlara İzin Ver' seçeneğini aktif etmelisiniz.")
                    .setPositiveButton("Ayarları Aç") { _, _ ->
                        val intent =
                            Intent(android.provider.Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM)
                        startActivity(intent)
                    }
                    .setNegativeButton("Vazgeç", null)
                    .show()
            }
        }

// Bildirimle gelindiyse planı göster
        checkTomorrowPlanIntent(intent)

// Bildirim sistemini başlat
        TomorrowPlanNotificationScheduler.scheduleNotification(this)
        //  val testIntent = Intent(this,TomorrowPlanAlarmReceiver::class.java)
        // sendBroadcast(testIntent)
    }

    override fun onResume() {
        super.onResume()
        if (hasLocationPermission()) {
            startLocationUpdates()
        }
    }

    private fun setupPermissionLauncher() {
        permissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
                val granted = permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true &&
                        permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true &&
                        (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                                permissions[Manifest.permission.POST_NOTIFICATIONS] == true)
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
        val coarse =
            ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fine == PackageManager.PERMISSION_GRANTED && coarse == PackageManager.PERMISSION_GRANTED
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun requestLocationPermission() {
        if (!isPermissionDialogShowing) {
            isPermissionDialogShowing = true
            val dialog = LocationPermissionDialogFragment {
                permissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.POST_NOTIFICATIONS
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
                val intent =
                    Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
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

    private fun checkTomorrowPlanIntent(intent: Intent?) {
        val showPlan = intent?.getBooleanExtra("SHOW_TOMORROW_PLAN", false) ?: false
        if (showPlan) {
            showTomorrowPlanBottomSheet()
        }
    }

    private fun showTomorrowPlanBottomSheet() {
        val repository = TomorrowPlanRepository(
            TravelDatabase.getDatabase(this).hotelDao(),
            TravelDatabase.getDatabase(this).restaurantDao(),
            TravelDatabase.getDatabase(this).transportDao(),
            TravelDatabase.getDatabase(this).rentDao(),
            TravelDatabase.getDatabase(this).museumDao()
        )

        val bottomSheet = TomorrowPlanBottomSheetDialogFragment(repository)
        bottomSheet.show(supportFragmentManager, "TomorrowPlanSheet")
    }

    private fun createNotificationChannelIfNeeded() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = android.app.NotificationChannel(
                "tomorrow_plan_channel",
                "Yarınki Plan Bildirimi",
                android.app.NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Yarın için plan hatırlatmaları."
            }
            val manager = getSystemService(android.app.NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun setupNotificationPermissionLauncher() {
        notificationPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    Log.d("NotificationPermission", "✅ Bildirim izni verildi")
                } else {
                    Log.w("NotificationPermission", "❌ Bildirim izni reddedildi")
                }
            }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkAndRequestNotificationPermission() {
        val permissionCheck = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.POST_NOTIFICATIONS
        )
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            notificationPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}