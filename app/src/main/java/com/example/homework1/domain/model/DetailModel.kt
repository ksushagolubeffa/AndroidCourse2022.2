package com.example.homework1.domain.model

import com.example.homework1.data.response.*

data class DetailModel(
    val base: String,
    val clouds: Clouds,
    val cod: Int,
    val dt: Int,
    val id: Int,
    val main: Main,
    val name: String,
    val sys: Sys,
    val timezone: Int,
    val visibility: Int,
    val weather: List<Weather>,
    val wind: Wind
)
