package com.example.homework1.data

import com.example.homework1.domain.repository.WeatherRepository
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi
) : WeatherRepository
{
    override suspend fun getWeather(city: String) = api.getWeather(city)

    override suspend fun getWeatherList(
        longitude: Double?,
        latitude: Double?,
        count: Int
    ) = api.getWeatherList(longitude, latitude, count)
}
