package com.gianlucaparadise.githubbrowser

import android.app.Application
import android.content.Context

class MainApplication : Application() {
    init {
        instance = this
    }

    companion object {
        private lateinit var instance: MainApplication

        val applicationContext: Context
            get() = instance.applicationContext
    }
}