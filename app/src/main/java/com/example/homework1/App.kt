package com.example.homework1

import android.app.Application
import com.example.homework1.di.module.*
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import timber.log.Timber

class App : Application(){
    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidContext(this@App)
            modules(
                appModule,
                networkModule,
                mainKoinModule
            )
        }
    }
}

