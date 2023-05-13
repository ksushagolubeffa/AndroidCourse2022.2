package com.example.homework1.di.module

import com.example.homework1.data.WeatherRepositoryImpl
import com.example.homework1.domain.repository.WeatherRepository
import com.example.homework1.domain.usecase.WeatherListUseCase
import com.example.homework1.domain.usecase.WeatherUseCase
import com.example.homework1.presentation.viewmodel.DetailViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainKoinModule = module {
    factory<WeatherRepository> { WeatherRepositoryImpl(get()) }
    factory { WeatherUseCase(get()) }
    factory { WeatherListUseCase(get()) }
//    viewModel { SearchViewModel(get()) }
    viewModel { DetailViewModel(get(), get()) }
}