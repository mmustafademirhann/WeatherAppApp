package com.example.vbchatgptweatherapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbchatgptweatherapp.RetrofitInstance
import com.example.vbchatgptweatherapp.data.modelModel.WeatherWeather
import com.example.vbchatgptweatherapp.data.modelModelModel.WtWtWeather
import com.example.vbchatgptweatherapp.domain.network.Response
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Locale

class WeatherViewModel : ViewModel() {
    private val _weather = MutableLiveData<Response<WtWtWeather>>()
    val weather: LiveData<Response<WtWtWeather>>get() = _weather

    private val _hourlyWeatherData = MutableLiveData<List<WtWtWeather.WWW>>()
    val hourlyWeatherData: LiveData<List<WtWtWeather.WWW>> get() = _hourlyWeatherData




    fun updateHourlyWeather(hourlyWeather: List<WtWtWeather.WWW>) {
        _hourlyWeatherData.value = hourlyWeather
    }


    fun fetchWeather(city: String) {




            //
            viewModelScope.launch {


                try {
                    //you should send to reporsitory
                    val response = RetrofitInstance.api.getWeather(RetrofitInstance.lat, RetrofitInstance.lon, RetrofitInstance.appid)
                    response.body()?.let {
                        _weather.postValue(Response.Success(it))




                    }
                    // e-ticaret

                    if (response.isSuccessful) {
                        Log.d("WeatherResponse", "Response: ${response.body()}")
                        response.body()?.let {
                            _weather.postValue(Response.Success(it))

                        }
                    } else {
                        Log.e("WeatherError", "Error: ${response.code()}")
                        _weather.postValue(Response.ErrorResponse(errorResponse = response.message().toString()))
                    }
                } catch (e: IOException) {
                    Log.e("WeatherError", "IOException: ${e.message}")
                    _weather.postValue(Response.Error(error("ıoe error aldınınz")))
                } catch (e: HttpException) {
                    Log.e("WeatherError", "HttpException: ${e.message}")
                    _weather.postValue(Response.Error(error("http error aldınız")))
                } catch (e: Exception) {
                    Log.e("WeatherError", "Exception: ${e.message}")
                    _weather.postValue(Response.Error(error("Exeption Error aldınız")))

                }
            }


    }
    //View types you should study about it
    fun groupWeatherByDay(weatherList: List<WtWtWeather.WWW>?): Map<String, List<WtWtWeather.WWW>> {
        // Create a date formatter to extract the day from the "dtTxt" field
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val dayFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Group the weather data by day
        return weatherList?.groupBy { weatherItem ->
            val date = dateFormatter.parse(weatherItem.dtTxt)
            dayFormatter.format(date)
        } ?: emptyMap()
    }


    fun getHoursForDay(weatherList: List<WtWtWeather.WWW>?, day: String): List<WtWtWeather.WWW> {
        // Create a date formatter to extract the day from the "dtTxt" field
        val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val dayFormatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // Filter the weather data to only include the items for the specified day
        return weatherList?.filter { weatherItem ->
            val date = dateFormatter.parse(weatherItem.dtTxt)
            dayFormatter.format(date) == day
        } ?: emptyList()
    }



}