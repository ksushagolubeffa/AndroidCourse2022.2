package com.example.homework1.presentation.rv

import android.annotation.SuppressLint
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.example.homework1.R
import com.example.homework1.databinding.ItemCityBinding
import com.example.homework1.domain.model.DetailModel

class CityHolder(
    private val binding: ItemCityBinding,
    private val glide: RequestManager,
    private val onItemClick: (String) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("ResourceAsColor", "SetTextI18n")
    fun onBind(city: DetailModel) {
        with(binding) {
            txtName.text = city.name
            txtTemp.text = "${city.main.temp} °C"
            var id = city.weather.firstOrNull()?.icon
            glide
                .load("https://openweathermap.org/img/w/$id.png")
                .placeholder(R.drawable.sun)
                .error(R.drawable.sun)
                .into(ivCity)

            root.setOnClickListener {
                onItemClick(city.name)
            }

            var temp = city.main.temp
            if (temp < -30) {
                txtTemp.setTextColor(R.color.very_low)
            } else if (temp >= -30 && temp < -5) {
                txtTemp.setTextColor(R.color.low)
            } else if (temp >= -5 && temp < 5) {
                txtTemp.setTextColor(R.color.normal)
            } else if (temp in 5.0..29.0) {
                txtTemp.setTextColor(R.color.high)
            } else {
                txtTemp.setTextColor(R.color.very_high)
            }
        }
    }
}
