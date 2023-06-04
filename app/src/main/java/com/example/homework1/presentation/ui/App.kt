package com.example.homework1.presentation.ui

import android.app.Application
import com.example.homework1.BuildConfig
import timber.log.Timber.DebugTree
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
