package com.example.homework1

import android.app.Application
import com.example.homework1.di.AppComponent
import com.example.homework1.di.DaggerAppComponent
import timber.log.Timber.DebugTree
import timber.log.Timber

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent
            .builder()
            .context(applicationContext)
            .build()

        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }


    companion object {

        lateinit var appComponent: AppComponent
    }
}
