package com.example.homework1.di.module

import com.example.homework1.BuildConfig
import com.example.homework1.data.WeatherApi
import com.example.homework1.di.qualifier.ApiKeyInterceptor
import com.example.homework1.di.qualifier.LoggingInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    @Provides
    @ApiKeyInterceptor
    fun apiKeyInterceptor(): Interceptor =
        Interceptor{
                chain ->
            val originalRequest = chain.request()
            originalRequest.url.newBuilder()
                .addQueryParameter("apiKey", API_KEY)
                .build()
                .let {
                    chain.proceed(
                        originalRequest.newBuilder().url(it).build()
                    ) }
        }

    @Provides
    @LoggingInterceptor
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
            .setLevel(
                HttpLoggingInterceptor.Level.BODY
            )
    }

    @Provides
    fun provideOkhttp(
        @ApiKeyInterceptor apiKeyInterceptor: Interceptor,
        @LoggingInterceptor loggingInterceptor: Interceptor,
    ): OkHttpClient =
        OkHttpClient.Builder()
            .addInterceptor(apiKeyInterceptor)
            .also {
                if (BuildConfig.DEBUG) {
                    it.addInterceptor(loggingInterceptor)
                }
            }
            .build()

    @Provides
    fun provideGsonConverter(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    fun provideWeatherApi(
        okHttpClient: OkHttpClient,
        gsonConverter: GsonConverterFactory,
    ): WeatherApi = Retrofit.Builder()
        .baseUrl(API_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverter)
        .build()
        .create(WeatherApi::class.java)

    companion object{
        private const val API_URL = BuildConfig.API_ENDPOINT
        private const val API_KEY = BuildConfig.API_KEY
    }
}
