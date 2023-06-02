package com.example.homework1.data

import com.example.homework1.data.mapper.toWeather
import com.example.homework1.data.mapper.toWeatherInfo
import com.example.homework1.domain.model.DetailModel
import com.example.homework1.domain.model.ListModel
import com.example.homework1.domain.repository.WeatherRepository
import io.reactivex.rxjava3.core.Single
import io.reactivex.rxjava3.schedulers.Schedulers

class WeatherRepositoryImpl (
    private val api: WeatherApi
) : WeatherRepository
{
    override fun getWeather(city: String): Single<DetailModel> = api.getWeather(city).map{
        it.toWeatherInfo()
    } .subscribeOn(Schedulers.io())

    override fun getWeatherList(
        longitude: Double?,
        latitude: Double?,
        count: Int
    ) :Single<ListModel> = api.getWeatherList(longitude, latitude, count).map{
        it.toWeather()
    }.subscribeOn(Schedulers.io())
}
