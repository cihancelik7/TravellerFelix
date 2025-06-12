package com.example.travellerfelix.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

object PermissionManager {

    fun hasConsent(context: Context): Boolean {
        return PreferenceHelper.hasUserGivenConsent(context)
    }

    fun isLocationPermissionGranted(context: Context): Boolean {
        val fine = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        val coarse = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION)
        return fine == PackageManager.PERMISSION_GRANTED && coarse == PackageManager.PERMISSION_GRANTED
    }

    fun shouldAskPermissionAgain(activity: Activity): Boolean {
        return ActivityCompat.shouldShowRequestPermissionRationale(
            activity, Manifest.permission.ACCESS_FINE_LOCATION
        )
    }

    fun requestLocationPermissions(activity: Activity, requestCode: Int = 1001) {
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            requestCode
        )
    }

    fun openSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }

    fun showPermissionSettingsDialog(activity: Activity) {
        AlertDialog.Builder(activity)
            .setTitle("İzin Gerekli")
            .setMessage("Yakındaki yerleri görebilmek için konum iznine ihtiyacımız var. Lütfen uygulama ayarlarından izni etkinleştirin.")
            .setPositiveButton("Ayarları Aç") { _, _ ->
                openSettings(activity)
            }
            .setNegativeButton("Vazgeç", null)
            .show()
    }

    fun handlePermissionFlow(
        activity: Activity,
        onGranted: () -> Unit,
        onDenied: () -> Unit
    ) {
        when {
            isLocationPermissionGranted(activity) -> {
                onGranted()
            }
            shouldAskPermissionAgain(activity) -> {
                showPermissionSettingsDialog(activity)
            }
            else -> {
                requestLocationPermissions(activity)
                onDenied()
            }
        }
    }
}