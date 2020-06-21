package com.gianlucaparadise.githubbrowser.util

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import com.gianlucaparadise.githubbrowser.MainApplication


object SharedPreferencesManager {

    private val preferences: SharedPreferences

    init {
        val context = MainApplication.applicationContext
        preferences = context.getSharedPreferences("private_preferences", MODE_PRIVATE)
    }

    private var _accessToken: String? = null
    var accessToken: String?
        get() {
            if (_accessToken == null) {
                _accessToken = preferences.getString("access_token", null)
            }
            return _accessToken
        }
        set(value) {
            // Apply method is asynchronous, but the backing field prevents race conditions
            preferences.edit().putString("access_token", value).apply()
            _accessToken = value
        }
}