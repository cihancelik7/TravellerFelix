package com.example.travellerfelix.utils

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat

object PermissionManager {

    fun hasConsent(context: Context): Boolean {
        return PreferenceHelper.hasUserGivenConsent(context)
    }

    fun showConsentRequireDialog(context: Context) {
        AlertDialog.Builder(context)
            .setTitle("İzin Gerekli")
            .setMessage("Bu özelliği kullanabilmek için KVKK onayı ve konum izni gereklidir. Lütfen ayarlardan izin verin!")
            .setPositiveButton("Ayarları Aç") { _, _ ->
                openSettings(context)
            }
            .setNegativeButton("Vazgeç", null)
            .show()
    }

    private fun openSettings(context: Context) {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", context.packageName, null)
        }
        context.startActivity(intent)
    }

    fun isLocationPermissionGranted(context: Context): Boolean {
        return ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }
}