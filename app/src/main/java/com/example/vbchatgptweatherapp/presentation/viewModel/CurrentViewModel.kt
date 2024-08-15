package com.example.vbchatgptweatherapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbchatgptweatherapp.RetrofitInstance
import com.example.vbchatgptweatherapp.data.modelModelModel.WeatherCurrent
import com.example.vbchatgptweatherapp.domain.network.Response
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class CurrentViewModel:ViewModel() {
    private val _weatherCurrent = MutableLiveData<Response<WeatherCurrent>>()
    val weatherCurrent: LiveData<Response<WeatherCurrent>>
    get() = _weatherCurrent

    fun fetchWeatherCurrent(city: String, latitude:Double,longitude:Double) {



        //



        viewModelScope.launch {

            _weatherCurrent.value = Response.LoadingState() // Emit LoadingState

            try {
                //you should send to reporsitory
                val response = RetrofitInstance.api.getWeatherCurrent(latitude, longitude, RetrofitInstance.appid)
                response.body()?.let {
                    _weatherCurrent.postValue(Response.Success(it))
                }
                // e-ticaret

                if (response.isSuccessful) {
                    Log.d("WeatherResponse", "Response: ${response.body()}")
                    response.body()?.let {
                        _weatherCurrent.postValue(Response.Success(it))

                    }
                } else {
                    Log.e("WeatherError", "Error: ${response.code()}")
                    _weatherCurrent.postValue(Response.ErrorResponse(errorResponse = response.message().toString()))
                }
            } catch (e: IOException) {
                Log.e("WeatherError", "IOException: ${e.message}")
                _weatherCurrent.postValue(Response.Error(error("ıoe error aldınınz")))
            } catch (e: HttpException) {
                Log.e("WeatherError", "HttpException: ${e.message}")
                _weatherCurrent.postValue(Response.Error(error("http error aldınız")))
            } catch (e: Exception) {
                Log.e("WeatherError", "Exception: ${e.message}")
                _weatherCurrent.postValue(Response.Error(error("Exeption Error aldınız")))

            }
        }


    }
}