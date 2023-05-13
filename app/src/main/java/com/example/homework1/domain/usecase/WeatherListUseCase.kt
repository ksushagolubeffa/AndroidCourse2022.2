package com.example.homework1.domain.usecase

import com.example.homework1.domain.model.ListModel
import com.example.homework1.domain.repository.WeatherRepository
import io.reactivex.rxjava3.core.Single
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

class WeatherListUseCase @Inject constructor(
    private val repository: WeatherRepository,
    private val scope: CoroutineDispatcher = Dispatchers.Main
) {

    operator fun invoke(
        latitude: Double?, longitude: Double?, count:Int
    ): Single<ListModel>  =
        repository.getWeatherList(latitude, longitude, count)
}
