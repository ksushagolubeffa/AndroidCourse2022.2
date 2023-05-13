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

//class SearchViewModel @Inject constructor(
//    private val weatherListUseCase: WeatherListUseCase
//): ViewModel()  {
//
//    private val _loading = MutableLiveData(false)
//    val loading: LiveData<Boolean>
//        get() = _loading
//
//    private var _weatherList:MutableLiveData<List<DetailModel>> = MutableLiveData()
//    val weatherList:LiveData<List<DetailModel>> = _weatherList
//
//    private val _error = MutableLiveData<Throwable?>(null)
//    val error: LiveData<Throwable?>
//        get() = _error
//
//    var weatherDisposable: Disposable? = null
//    var disposable: CompositeDisposable = CompositeDisposable()
//
//    private fun getWeatherList(latitude:Double?, longitude:Double?){
//        weatherDisposable = weatherListUseCase(latitude,longitude, 10)
//            .observeOn(AndroidSchedulers.mainThread())
//            .doOnSubscribe { _loading.value = true }
//            .doAfterTerminate { _loading.value = false }
//            .subscribeBy(onSuccess = { weatherInfo ->
//                _weatherList.value = weatherInfo
//                Log.e("SearchViewModel", "all good")
//            }, onError = { error ->
//                Log.e("SearchViewModel", "all bad")
//                _error.postValue(error)
//            })
//    }
//
//    fun onLoadClick(latitude:Double?,longitude:Double?) {
//        getWeatherList(latitude, longitude)
//    }
//
////    fun getWeather(city: String) {
////        weatherDisposable = weatherUseCase(city)
////            .observeOn(AndroidSchedulers.mainThread())
////            .doOnSubscribe { _loading.value = true }
////            .doAfterTerminate { _loading.value = false }
////            .subscribeBy(onSuccess = { weatherInfo ->
////                _weather.value = weatherInfo
////            }, onError = { error ->
////                _error.postValue(error)
////            })
////
////    }
//
//    override fun onCleared() {
//        super.onCleared()
//        weatherDisposable?.dispose()
//        disposable.clear()
//    }
//
//    companion object{
//        fun provideFactory(
//            weatherListUseCase: WeatherListUseCase
//        ): ViewModelProvider.Factory = viewModelFactory {
//            initializer {
//                SearchViewModel(weatherListUseCase)
//            }
//        }
//    }
//
////    viewModelScope.launch {
////        try {
////            val weather = weatherUseCase(city)
////            _weather.value = Result.success(weather)
////        } catch (ex: Exception) {
////            _weather.value = Result.failure(ex)
////            _error.value = ex
////            Timber.e("SearchViewModel error")
////        }
////    }
//
//    //        viewModelScope.launch {
////            try {
////                val weatherList = weatherListUseCase(latitude, longitude, 10)
////                _weatherList.value = Result.success(weatherList)
////            } catch (ex: Exception) {
////                _weatherList.value = Result.failure(ex)
////                _error.value = ex
////                Timber.e("SearchViewModel error")
////            }
////        }
//
//}
