package com.example.homework1.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.homework1.domain.model.DetailModel
import com.example.homework1.domain.usecase.WeatherUseCase
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase
): ViewModel() {

    private var _weather: MutableLiveData<Result<DetailModel>> = MutableLiveData()
    val weather: LiveData<Result<DetailModel>> = _weather

    private var _error: MutableLiveData<Exception> = MutableLiveData()

    suspend fun getWeatherByName(city: String) {
        viewModelScope.launch {
            try {
                val weather = weatherUseCase(city)
                _weather.value = Result.success(weather)
            } catch (ex: Exception) {
                _weather.value = Result.failure(ex)
                _error.value = ex
            }
        }
    }
}
