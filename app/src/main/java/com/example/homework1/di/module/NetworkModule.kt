package com.example.homework1.di.module

import com.example.homework1.BuildConfig
import com.example.homework1.data.WeatherApi
import com.example.homework1.data.interceptor.ApiKeyInterceptor
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


val networkModule = module {
    factory<Interceptor>(named("ApiKey")) {
        ApiKeyInterceptor()
    }
    factory<Interceptor>(named("Logging")) {
        HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }
    single { GsonConverterFactory.create() }
    single(qualifier = named("base_url")) { BuildConfig.API_ENDPOINT }
    single {
        provideHttpClient(get(named("ApiKey")), get(named("Logging")))
    }
    single {
        provideRetrofit(
            httpClient = get(),
            gsonFactory = get(),
            baseUrl = get(named("base_url"))
        )
    }
    factory {
        get<Retrofit>().create(WeatherApi::class.java)
    }
}

private fun provideHttpClient(
    apiKeyInterceptor: Interceptor,
    loggingInterceptor: Interceptor,
): OkHttpClient = OkHttpClient.Builder()
    .addInterceptor(loggingInterceptor)
    .addInterceptor(apiKeyInterceptor)
    .connectTimeout(10L, TimeUnit.SECONDS)
    .build()

private fun provideRetrofit(
    httpClient: OkHttpClient,
    gsonFactory: GsonConverterFactory,
    baseUrl: String,
): Retrofit = Retrofit.Builder()
    .client(httpClient)
    .baseUrl(baseUrl)
    .addConverterFactory(gsonFactory)
    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
    .build()
