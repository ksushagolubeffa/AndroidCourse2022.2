package com.example.homework1.data

import com.example.homework1.domain.model.DetailModel
import com.example.homework1.domain.model.ListModel
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather?units=metric")
    suspend fun getWeather(
        @Query("q") city: String
    ): DetailModel

    @GET("find?units=metric&lang=ru")
    suspend fun getWeatherList(
        @Query("lat") latitude: Double?,
        @Query("lon") longitude: Double?,
        @Query("cnt") count: Int
    ): ListModel
}
