package com.example.homework1.domain.usecase

import com.example.homework1.domain.model.DetailModel
import com.example.homework1.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherUseCase @Inject constructor(
    private val repository: WeatherRepository,
    private val scope: CoroutineDispatcher = Dispatchers.Main
) {

    suspend operator fun invoke(city: String): DetailModel =
        withContext(scope) {
            repository.getWeather(city)
        }
}
