package com.example.vbchatgptweatherapp.domain.network

import com.example.vbchatgptweatherapp.data.modelModel.WeatherWeather
import com.example.vbchatgptweatherapp.data.modelModelModel.WtWtWeather
import com.example.vbchatgptweatherapp.data.models.WeatherResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApi {
    @GET("forecast")
    suspend fun getWeather(
        @Query("lat") lat:Double,
        @Query("lon") lon:Double,
        @Query("appid") apiKey: String,
      //  @Query("units") units: String = "metric"
    ): Response<WtWtWeather>
}
//