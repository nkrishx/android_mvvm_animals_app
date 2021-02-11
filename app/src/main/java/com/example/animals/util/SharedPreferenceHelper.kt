package com.example.animals.util

import android.content.Context
import android.preference.PreferenceManager

class SharedPreferenceHelper(context: Context) {
    private val PREF_API_KEY = "Api key"

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context.applicationContext)

    //shared preference is a simple storage available as key value pairs, below function puts the value of the api key in PREF_API_KEY
    fun saveAPIKey(key: String?)
    {
        prefs.edit().putString(PREF_API_KEY, key)
    }

    fun getAPIKey() = prefs.getString(PREF_API_KEY, null)

}