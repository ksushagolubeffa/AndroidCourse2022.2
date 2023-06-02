package com.example.homework1.di.module

import com.example.homework1.BuildConfig
import com.example.homework1.data.WeatherApi
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Named
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    @Named("apiKeyInt")
    fun provideApiKeyInterceptor(
        @Named ("apiKey") apiKey: String
    ): Interceptor =
        Interceptor{
                chain ->
            val originalRequest = chain.request()
            originalRequest.url.newBuilder()
                .addQueryParameter("apiKey", apiKey)
                .build()
                .let {
                    chain.proceed(
                        originalRequest.newBuilder().url(it).build()
                    ) }
        }

    @Provides
    @Singleton
    @Named("logger")
    fun provideLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
            .setLevel(
                HttpLoggingInterceptor.Level.BODY
            )
    }

    @Provides
    @Singleton
    fun provideHttpClient(
        @Named("apiKeyInt") apiKeyInterceptor: Interceptor,
        @Named("logger") loggingInterceptor: Interceptor,
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
    @Singleton
    fun provideGsonConverter(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Singleton
    fun provideWeatherApi(
        okHttpClient: OkHttpClient,
        gsonConverter: GsonConverterFactory,
        @Named ("baseUrl") baseUrl: String,
    ): WeatherApi = Retrofit.Builder()
        .baseUrl(baseUrl)
        .client(okHttpClient)
        .addConverterFactory(gsonConverter)
        .build()
        .create(WeatherApi::class.java)

    @Provides
    @Singleton
    @Named("baseUrl")
    fun provideBaseUrl(): String = BuildConfig.API_ENDPOINT

    @Provides
    @Singleton
    @Named("apiKey")
    fun provideApiKey(): String = BuildConfig.API_KEY

}
