package com.example.vbchatgptweatherapp

import com.example.vbchatgptweatherapp.domain.network.WeatherApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
//https://api.openweathermap.org/data/2.5/forecast/daily?lat=41.01&lon=28.955&cnt=1&appid=af4706d9363e8118bbb8d640be5a833b
//https://api.openweathermap.org/data/2.5/weather?lat=41.01&lon=28.955&appid=042c2ddfb3f95fa918336e6edbd4fe63
//https://api.openweathermap.org/data/2.5/forecast?cnt=5&lat=44.34&lon=10.99&appid=042c2ddfb3f95fa918336e6edbd4fe63
//  api.openweathermap.org/data/2.5/forecast/daily?lat={lat}&lon={lon}&cnt={cnt}&appid={API key}
object RetrofitInstance {
    const val BASE_URL = "https://api.openweathermap.org/data/2.5/"
    const val appid: String = "042c2ddfb3f95fa918336e6edbd4fe63"
    const val METRIC_UNIT: String = "metric"
    const val lat:Double=-54.93
    const val lon:Double=-67.61




    val api: WeatherApi by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WeatherApi::class.java)
    }
}