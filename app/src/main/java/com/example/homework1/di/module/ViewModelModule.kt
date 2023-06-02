package com.example.homework1.di.module

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.homework1.di.ViewModelKey
import com.example.homework1.presentation.viewmodel.DetailViewModel
import com.example.homework1.presentation.viewmodel.SearchViewModel
import com.example.homework1.utils.ViewModelFactory
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    fun bindViewModelFactory(
        factory: ViewModelFactory
    ): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(DetailViewModel::class)
    fun provideDetailViewModel(viewModel: DetailViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(SearchViewModel::class)
    fun provideSearchViewViewModel(viewModel: SearchViewModel): ViewModel
}
