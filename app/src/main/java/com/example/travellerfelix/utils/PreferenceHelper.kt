package com.example.travellerfelix.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {
    private const val PREF_NAME = "travellerfelix_prefs"
    private const val IS_FIRST_TIME = "isFirstTime"
    private const val USER_CONSENT = "userConsent" // KVKK onayı için
    private const val KEY_DEFERRED_LOCATION = "deferred_location"

    private fun getPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun isFirstTime(context: Context): Boolean {
        return getPreferences(context).getBoolean(IS_FIRST_TIME, true)
    }

    fun setNoFirstTime(context: Context) {
        getPreferences(context).edit().putBoolean(IS_FIRST_TIME, false).apply()
    }

    fun setUserConsent(context: Context, isAccepted: Boolean) {
        getPreferences(context).edit().putBoolean(USER_CONSENT, isAccepted).apply()
    }

    fun hasUserGivenConsent(context: Context): Boolean {
        return getPreferences(context).getBoolean(USER_CONSENT, false)
    }
    fun setUserDeferredLocationPermission(context: Context, deferred: Boolean) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .putBoolean(KEY_DEFERRED_LOCATION, deferred)
            .apply()
    }

    fun isLocationPermissionDeferred(context: Context): Boolean {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .getBoolean(KEY_DEFERRED_LOCATION, false)
    }

    fun clearDeferredLocationPermission(context: Context) {
        context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
            .edit()
            .remove(KEY_DEFERRED_LOCATION)
            .apply()
    }
}