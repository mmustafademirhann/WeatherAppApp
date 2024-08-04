package com.example.vbchatgptweatherapp.data.modelModel


import com.google.gson.annotations.SerializedName

data class WeatherWeather(
    @SerializedName("city")
    val city: City?,
    @SerializedName("cnt")
    val cnt: Int?,
    @SerializedName("cod")
    val cod: String?,
    @SerializedName("list")
    val list: List<WeatherData>?,
    @SerializedName("message")
    val message: Int?
) {



}