package com.example.homework1.domain.repository

import com.example.homework1.domain.model.DetailModel
import com.example.homework1.domain.model.ListModel

interface WeatherRepository {
    suspend fun getWeather(city: String): DetailModel
    suspend fun getWeatherList(longitude: Double?, latitude: Double?, count: Int): ListModel
}
