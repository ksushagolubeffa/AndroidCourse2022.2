package com.example.homework1.di

import android.content.Context
import com.example.homework1.di.module.AppModule
import com.example.homework1.di.module.NetworkModule
import com.example.homework1.di.module.RepositoryModule
import com.example.homework1.di.module.ViewModelModule
import com.example.homework1.presentation.ui.InfoFragment
import com.example.homework1.presentation.ui.SearchingFragment
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Component(modules =
                    [AppModule::class,
                    NetworkModule::class,
                    RepositoryModule::class,
                    ViewModelModule::class])
@Singleton

interface AppComponent {

    fun inject(searchingFragment: SearchingFragment)
    fun inject(infoFragment: InfoFragment)

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun context(applicationContext: Context): Builder

        fun build(): AppComponent
    }
}
