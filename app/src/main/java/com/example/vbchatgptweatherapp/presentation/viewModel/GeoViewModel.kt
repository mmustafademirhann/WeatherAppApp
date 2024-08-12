package com.example.vbchatgptweatherapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbchatgptweatherapp.RetrofitInstance
import com.example.vbchatgptweatherapp.data.modelModelModel.GeoModel
import com.example.vbchatgptweatherapp.data.modelModelModel.WeatherCurrent
import com.example.vbchatgptweatherapp.domain.network.Response
import com.example.vbchatgptweatherapp.repository.GeograpyRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class GeoViewModel(val repository: GeograpyRepository): ViewModel() {

    constructor():this(GeograpyRepository(RetrofitInstance.api))private val _cities = MutableLiveData<Response<GeoModel>>()
    val cities: LiveData<Response<GeoModel>> get() = _cities


    suspend fun loadGegraphy(q:String,limit:Int){
        repository.getGeographies(q,limit)
    }
    private val _weatherGeo = MutableLiveData<Response<GeoModel>>()
    val weatherCurrent: LiveData<Response<GeoModel>>
        get() = _weatherGeo

    fun fetchGeoCurrent(query: String, limit: Int) {

        //
        viewModelScope.launch {
            // @Query("q") q:String,
            //        @Query("limit") limit:Int,
            //        @Query("appid") ApiKey: String,

            try {
                //you should send to reporsitory
                val response = RetrofitInstance.api.getGeo(RetrofitInstance.q, RetrofitInstance.limit,RetrofitInstance.appid)
                response.body()?.let {
                    _weatherGeo.postValue(weatherCurrent.value)
                }
                // e-ticaret

                if (response.isSuccessful) {
                    Log.d("WeatherResponse", "Response: ${response.body()}")
                    response.body()?.let {
                        _weatherGeo.postValue(weatherCurrent.value)

                    }
                } else {
                    Log.e("WeatherError", "Error: ${response.code()}")
                    _weatherGeo.postValue(Response.ErrorResponse(errorResponse = response.message().toString()))
                }
            } catch (e: IOException) {
                Log.e("WeatherError", "IOException: ${e.message}")
                _weatherGeo.postValue(Response.Error(error("ıoe error aldınınz")))
            } catch (e: HttpException) {
                Log.e("WeatherError", "HttpException: ${e.message}")
                _weatherGeo.postValue(Response.Error(error("http error aldınız")))
            } catch (e: Exception) {
                Log.e("WeatherError", "Exception: ${e.message}")
                _weatherGeo.postValue(Response.Error(error("Exeption Error aldınız")))
            }
        }
    }
}