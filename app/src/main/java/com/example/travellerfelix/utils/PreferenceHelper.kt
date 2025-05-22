package com.example.travellerfelix.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {
    private const val PREF_NAME = "travellerfelix_prefs"
    private const val IS_FIRST_TIME = "isFirstTime"

    private fun getPreferences(context: Context):SharedPreferences{
        return context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE)
    }
    fun isFirstTime(context: Context) : Boolean{
        return getPreferences(context).getBoolean(IS_FIRST_TIME,true)
    }
    fun setNoFirstTime(context: Context){
        getPreferences(context).edit().putBoolean(IS_FIRST_TIME,false).apply()
    }
}