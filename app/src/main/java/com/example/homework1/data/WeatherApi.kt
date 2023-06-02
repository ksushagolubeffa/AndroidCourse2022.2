package com.example.homework1.data

import com.example.homework1.data.response.WeatherResponse
import com.example.homework1.data.response.WeatherResponseList
import io.reactivex.rxjava3.core.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherApi {

    @GET("weather?units=metric")
    fun getWeather(
        @Query("q") city: String
    ): Single<WeatherResponse>

    @GET("find?units=metric&lang=ru")
    fun getWeatherList(
        @Query("lat") latitude: Double?,
        @Query("lon") longitude: Double?,
        @Query("cnt") count: Int
    ): Single<WeatherResponseList>
}
