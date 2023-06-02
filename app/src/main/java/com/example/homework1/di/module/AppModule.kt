package com.example.homework1.di.module

import com.example.homework1.utils.AndroidResourceProvider
import com.example.homework1.utils.ResourceProvider
import com.google.android.gms.location.LocationServices

import org.koin.dsl.module

val appModule = module {
    factory<ResourceProvider> {
        AndroidResourceProvider(get())
    }
    single {
        LocationServices.getFusedLocationProviderClient(get())
    }
}
