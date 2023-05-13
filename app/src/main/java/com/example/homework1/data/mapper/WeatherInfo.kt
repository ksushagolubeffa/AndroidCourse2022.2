package com.example.homework1.data.mapper

import android.util.Log
import androidx.lifecycle.Transformations.map
import com.example.homework1.data.response.*
import com.example.homework1.domain.model.DetailModel
import com.example.homework1.domain.model.ListModel

fun WeatherResponse.toWeatherInfo(): DetailModel = DetailModel(
    base = base,
    clouds = clouds,
    cod = cod,
    dt = dt,
    id = id,
    main = main,
    name = name,
    sys = sys,
    timezone = timezone,
    visibility = visibility,
    weather = weather,
    wind = wind
)

//fun List<WeatherResponse>.toWeathers(): List<DetailModel> = map{
//    it.toWeatherInfo()
//}

fun WeatherResponseList.toWeather(): ListModel {
    val detailModels = mutableListOf<DetailModel>()
    this.list.forEach { weatherResponse ->
        detailModels.add(weatherResponse.toWeatherInfo())
    }
    return ListModel(detailModels)
}
