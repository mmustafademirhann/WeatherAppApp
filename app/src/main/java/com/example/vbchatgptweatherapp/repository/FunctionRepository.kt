package com.example.vbchatgptweatherapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.vbchatgptweatherapp.data.modelModelModel.WtWtWeather
import com.example.vbchatgptweatherapp.domain.network.Response

class FunctionRepository {
    private val _weather = MutableLiveData<Response<WtWtWeather>>()
    val weather: LiveData<Response<WtWtWeather>>
        get() = _weather




    fun isUnderOneCelsius(tempKelvin: Double?): Boolean {
        val tempCelsius = tempKelvin?.minus(273.15)
        return tempCelsius != null && tempCelsius < 1
    }

    fun isNight():Boolean
    {

        return true
    }
    fun isUnderOneCelcius():Boolean{

        return true
    }
    fun isCloudy(weatherCondition: String?):Boolean
    {


        return weatherCondition?.contains("Cloud", ignoreCase = true) == true
    }
    fun isSunny():Boolean
    {
        return true
    }

    fun isHaze():Boolean
    {
        return true
    }
    fun isSnowy():Boolean
    {
        return true
    }





}