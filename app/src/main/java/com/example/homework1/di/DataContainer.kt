package com.example.homework1.di

import com.example.homework1.BuildConfig
import com.example.homework1.data.WeatherApi
import com.example.homework1.data.WeatherRepositoryImpl
import com.example.homework1.data.interceptor.ApiKeyInterceptor
import com.example.homework1.domain.repository.WeatherRepository
import com.example.homework1.domain.usecase.WeatherListUseCase
import com.example.homework1.domain.usecase.WeatherUseCase
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object DataContainer {

    private const val BASE_URL = BuildConfig.API_ENDPOINT

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }

    private val httpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor(ApiKeyInterceptor())
            .build()
    }

    private val retrofit by lazy {
        Retrofit.Builder()
            .client(httpClient)
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val weatherApi = retrofit.create(WeatherApi::class.java)

    private val weatherRepository: WeatherRepository = WeatherRepositoryImpl(
        api = weatherApi,
    )

    val weatherListUseCase: WeatherListUseCase = WeatherListUseCase(
        repository = weatherRepository
    )
    val weatherUseCase: WeatherUseCase = WeatherUseCase(
        repository= weatherRepository
    )
}