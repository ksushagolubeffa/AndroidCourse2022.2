package com.example.homework1.utils

interface ResourceProvider {

    fun getString(id: Int): String

    fun getColor(id: Int): Int
}
