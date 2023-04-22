package com.example.homework1.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.homework1.domain.model.DetailModel
import com.example.homework1.domain.model.ListModel
import com.example.homework1.domain.usecase.WeatherListUseCase
import com.example.homework1.domain.usecase.WeatherUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase,
    private val weatherListUseCase: WeatherListUseCase
): ViewModel()  {

    private var _weather: MutableLiveData<Result<DetailModel>> = MutableLiveData()
    val weather: LiveData<Result<DetailModel>> = _weather

    private var _weatherList:MutableLiveData<Result<ListModel>> = MutableLiveData()
    val weatherList:LiveData<Result<ListModel>> = _weatherList

    private var _error: MutableLiveData<Exception> = MutableLiveData()

    fun getWeatherList(latitude:Double?,longitude:Double?){
        viewModelScope.launch {
            try {
                val weatherList = weatherListUseCase(latitude, longitude, 10)
                _weatherList.value = Result.success(weatherList)
            } catch (ex: Exception) {
                _weatherList.value = Result.failure(ex)
                _error.value = ex
                Timber.e("SearchViewModel error")
            }
        }
    }

    suspend fun getWeather(city: String) {
        viewModelScope.launch {
            try {
                val weather = weatherUseCase(city)
                _weather.value = Result.success(weather)
            } catch (ex: Exception) {
                _weather.value = Result.failure(ex)
                _error.value = ex
                Timber.e("SearchViewModel error")
            }
        }
    }

}
