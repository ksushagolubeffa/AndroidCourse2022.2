package com.example.homework1.presentation.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.homework1.R
import com.example.homework1.data.DataContainer
import com.example.homework1.data.WeatherRepositoryImpl
import com.example.homework1.databinding.FragmentInfoBinding
import com.example.homework1.domain.model.DetailModel
import com.example.homework1.domain.usecase.WeatherUseCase
import com.example.homework1.presentation.utils.ViewModelFactory
import com.example.homework1.presentation.viewmodel.DetailViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat

class InfoFragment : Fragment(R.layout.fragment_info) {

    private var binding: FragmentInfoBinding? = null
    private val repository = WeatherRepositoryImpl(DataContainer.weatherApi)
    private lateinit var viewModel: DetailViewModel
    private var detailModel: DetailModel? = null
    private var weatherUseCase = WeatherUseCase(repository = repository)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initFactory()
        initObservers()
        binding = FragmentInfoBinding.bind(view)
        val name = arguments?.getString("CITY_NAME")!!
        initWeather(name)
    }

    @SuppressLint("SetTextI18n", "SimpleDateFormat")
    private fun loadWeather(query: String): String? {
        var event: String? = null
        lifecycleScope.launch {
            try {
                weatherUseCase(query).also {
                    binding?.txtName?.text = it.name
                    binding?.txtTemp?.text = "${it.main.temp.toInt()} °C"
                    binding?.txtDesc?.text = it.weather.firstOrNull()?.description
                    binding?.txtTmpM?.text =
                        "Min : ${it.main.tempMin.toInt()}°C; Max : ${it.main.tempMax.toInt()}°C"
                    binding?.txtH?.text = "Humidity: ${it.main.humidity}%"
                    binding?.txtDir?.text = "Direction: ${getDirection(it.wind.deg)}"
                    binding?.txtSpeed?.text = "Speed:       ${it.wind.speed} m/s"
                    binding?.txtPres?.text = "Pressure: ${(it.main.pressure / 1.333).toInt()} mm"
                    binding?.txtRise?.text =
                        "Sunrise: ${SimpleDateFormat("HH:mm").format(it.sys.sunrise * 1000)}"
                    binding?.txtSet?.text =
                        "Sunset: ${SimpleDateFormat("HH:mm").format(it.sys.sunset * 1000)}"
                    changeBackground(it.weather.firstOrNull()?.main)
                }
            } catch (error: Throwable) {
                Timber.e("error")
            }
        }
        return event
    }

    private fun getDirection(degree: Int): String {
        return when (degree) {
            in 0..10 -> "N"
            in 349..359 -> "N"
            in 11..33 -> "NNE"
            in 34..55 -> "NE"
            in 56..78 -> "ENE"
            in 79..100 -> "E"
            in 101..123 -> "ESE"
            in 124..145 -> "SE"
            in 146..168 -> "SSE"
            in 169..190 -> "S"
            in 191..213 -> "SSW"
            in 214..235 -> "SW"
            in 236..258 -> "WSW"
            in 259..280 -> "W"
            in 281..303 -> "WNW"
            in 304..325 -> "NW"
            else -> "NNW"
        }
    }

    private fun changeBackground(event: String?) {
        when (event) {
            "Rain" -> view?.setBackgroundResource(R.drawable.rain)
            "Snow" -> view?.setBackgroundResource(R.drawable.snow)
            "Thunderstorm" -> view?.setBackgroundResource(R.drawable.thunderstorm)
            "Drizzle" -> view?.setBackgroundResource(R.drawable.drizzle)
            "Clear" -> view?.setBackgroundResource(R.drawable.clear)
            "Clouds" -> view?.setBackgroundResource(R.drawable.clouds)
            else -> view?.setBackgroundResource(R.drawable.mist)
        }
    }

    private fun initObservers() {
        viewModel.weather.observe(viewLifecycleOwner) { it ->
            it.fold(
                onSuccess =
                {
                    detailModel = it
                    loadWeather(it.name)
                },
                onFailure = { Timber.e("error") })
        }
    }

    private fun initFactory() {
        val factory = ViewModelFactory(DataContainer)
        viewModel = ViewModelProvider(
            this,
            factory
        )[DetailViewModel::class.java]
    }

    private fun initWeather(cityTitle: String) {
        lifecycleScope.launch {
            viewModel.getWeatherByName(cityTitle)
        }
    }
}
