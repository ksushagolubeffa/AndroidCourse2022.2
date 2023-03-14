package com.example.homework1.domain.usecase

import com.example.homework1.domain.model.DetailModel
import com.example.homework1.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class WeatherUseCase(
    private val repository: WeatherRepository,
    private val scope: CoroutineDispatcher = Dispatchers.Main
) {

    suspend operator fun invoke(city: String): DetailModel =
        withContext(scope) {
            repository.getWeather(city)
        }
}
