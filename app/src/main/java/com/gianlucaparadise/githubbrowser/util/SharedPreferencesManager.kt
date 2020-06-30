package com.gianlucaparadise.githubbrowser.util

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharedPreferencesManager @Inject constructor (@ApplicationContext private val context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("private_preferences", MODE_PRIVATE)

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