package com.example.vbchatgptweatherapp.data.daysHandling

import com.example.vbchatgptweatherapp.data.modelModelModel.WtWtWeather

data class WeatherItem(


                       val hourlyWeather: List<WtWtWeather.WWW>,
                       val date: String,
                       var isExpanded:Boolean=false
)