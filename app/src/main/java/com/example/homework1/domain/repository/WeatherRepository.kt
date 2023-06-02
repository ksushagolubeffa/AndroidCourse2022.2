package com.example.homework1.domain.repository

import com.example.homework1.domain.model.DetailModel
import com.example.homework1.domain.model.ListModel
import io.reactivex.rxjava3.core.Single

interface WeatherRepository {
    fun getWeather(city: String): Single<DetailModel>
    fun getWeatherList(longitude: Double?, latitude: Double?, count: Int): Single<ListModel>
}
