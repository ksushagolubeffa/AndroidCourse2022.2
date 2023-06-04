package com.example.homework1.presentation.viewmodel

import androidx.lifecycle.*
import com.example.homework1.domain.model.DetailModel
import com.example.homework1.domain.usecase.WeatherUseCase
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class DetailViewModel @AssistedInject constructor(
    @Assisted private val city: String,
    private val weatherUseCase: WeatherUseCase
): ViewModel() {

    private var _weather: MutableLiveData<Result<DetailModel>> = MutableLiveData()
    val weather: LiveData<Result<DetailModel>> = _weather

    private var _error: MutableLiveData<Exception> = MutableLiveData()

    suspend fun getWeatherByName() {
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

    @AssistedFactory
    interface Factory {
        fun create(city: String): DetailViewModel
    }

    companion object {
        fun provideFactory(
            assistedFactory: Factory,
            city: String
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                assistedFactory.create(city) as T
        }
    }
}
