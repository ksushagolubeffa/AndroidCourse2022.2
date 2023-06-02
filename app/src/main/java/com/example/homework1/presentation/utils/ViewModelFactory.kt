package com.example.homework1.presentation.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.homework1.R
import com.example.homework1.data.DataContainer
import com.example.homework1.presentation.viewmodel.DetailViewModel
import com.example.homework1.presentation.viewmodel.SearchViewModel

class ViewModelFactory(
    private val di: DataContainer,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T =
        when {
            modelClass.isAssignableFrom(SearchViewModel::class.java) ->
                SearchViewModel(di.weatherUseCase, di.weatherListUseCase)
                        as? T ?: throw IllegalArgumentException(R.string.error_txt.toString())
            modelClass.isAssignableFrom(DetailViewModel::class.java) ->
                DetailViewModel(di.weatherUseCase)
                        as? T ?: throw IllegalArgumentException(R.string.error_txt.toString())
            else ->
                throw IllegalArgumentException(R.string.error_txt.toString())
        }
}
