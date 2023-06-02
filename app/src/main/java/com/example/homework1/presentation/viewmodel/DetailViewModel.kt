package com.example.homework1.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.*
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.homework1.domain.model.DetailModel
import com.example.homework1.domain.model.ListModel
import com.example.homework1.domain.usecase.WeatherListUseCase
import com.example.homework1.domain.usecase.WeatherUseCase
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.kotlin.subscribeBy
import javax.inject.Inject

class DetailViewModel @Inject constructor(
    private val weatherUseCase: WeatherUseCase,
    private val weatherListUseCase: WeatherListUseCase
): ViewModel() {

    private val _loading = MutableLiveData(false)
    val loading: LiveData<Boolean>
        get() = _loading

    private val _weather= MutableLiveData<DetailModel>(null)
    val weather: LiveData<DetailModel>
        get() = _weather

    private val _weatherList= MutableLiveData<ListModel>(null)
    val weatherList: LiveData<ListModel>
        get() = _weatherList

    private val _error = MutableLiveData<Throwable?>(null)
    val error: LiveData<Throwable?>
        get() = _error

    fun onLoadClick(city: String) {
        getWeather(city)
    }

    fun onLoadClickList(latitude: Double?, longitude: Double?) {
        getWeatherList(latitude, longitude)
    }

    var weatherDisposable: Disposable? = null
    var disposable: CompositeDisposable = CompositeDisposable()

    private fun getWeather(city: String) {
        weatherDisposable = weatherUseCase(city)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _loading.value = true }
            .doAfterTerminate { _loading.value = false }
            .subscribeBy(onSuccess = { weatherInfo ->
                _weather.value = weatherInfo
            }, onError = { error ->
                _error.postValue(error)
            })
    }

    private fun getWeatherList(latitude:Double?, longitude:Double?){
        weatherDisposable = weatherListUseCase(latitude,longitude, 10)
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { _loading.value = true }
            .doAfterTerminate { _loading.value = false }
            .subscribeBy(onSuccess = { weatherInfo ->
                _weatherList.value = weatherInfo
            }, onError = { error ->
                Log.e("DetailViewModel", Log.getStackTraceString(error))
                _error.postValue(error)
            })
    }

    override fun onCleared() {
        super.onCleared()
        weatherDisposable?.dispose()
        disposable.clear()
    }

    companion object {
        fun provideFactory(
            weatherUseCase: WeatherUseCase,
            weatherListUseCase: WeatherListUseCase
        ): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                DetailViewModel(weatherUseCase, weatherListUseCase)
            }
        }
    }
}
