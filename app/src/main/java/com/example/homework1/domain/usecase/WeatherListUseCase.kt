package com.example.homework1.domain.usecase

import com.example.homework1.domain.model.ListModel
import com.example.homework1.domain.repository.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WeatherListUseCase @Inject constructor(
    private val repository: WeatherRepository,
    private val scope: CoroutineDispatcher = Dispatchers.Main
) {

    suspend operator fun invoke(latitude: Double?, longitude: Double?, count:Int): ListModel =
        withContext(scope) {
            repository.getWeatherList(latitude, longitude, count)
        }
}
