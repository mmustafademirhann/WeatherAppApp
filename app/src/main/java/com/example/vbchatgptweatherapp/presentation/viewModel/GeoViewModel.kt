package com.example.vbchatgptweatherapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbchatgptweatherapp.RetrofitInstance
import com.example.vbchatgptweatherapp.RetrofitInstanceForGeo
import com.example.vbchatgptweatherapp.data.modelModelModel.GeoModel
import com.example.vbchatgptweatherapp.data.modelModelModel.WeatherCurrent
import com.example.vbchatgptweatherapp.data.modelModelModel.trdTry.GeographiaCityModels
import com.example.vbchatgptweatherapp.domain.network.Response
import com.example.vbchatgptweatherapp.repository.GeograpyRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class GeoViewModel(val repository: GeograpyRepository): ViewModel() {

    constructor():this(GeograpyRepository(RetrofitInstance.api))private val _cities = MutableLiveData<Response<GeographiaCityModels>>()
    val cities: LiveData<Response<GeographiaCityModels>> get() = _cities


    suspend fun loadGegraphy(q:String,limit:Int){
        repository.getGeographies(q,limit)
    }
    private val _weatherGeo = MutableLiveData<Response<GeoModel>>()
    val weatherCurrent: LiveData<Response<GeoModel>>
        get() = _weatherGeo

    fun fetchGeoCurrent(query: String, limit: Int) {


        viewModelScope.launch {
            // @Query("q") q:String,
            // @Query("limit") limit:Int,
            // @Query("appid") ApiKey: String,

            try {
                //you should send to reporsitory
                val response = RetrofitInstanceForGeo.api.getGeo(RetrofitInstanceForGeo.q, RetrofitInstanceForGeo.limit,RetrofitInstanceForGeo.appid)
                response.body()?.let {
                    _weatherGeo.postValue(weatherCurrent.value)
                }
                // e-ticaret

                if (response.isSuccessful) {
                    Log.d("WeatherResponse", "Response: ${response.body()}")
                    response.body()?.let {
                        _weatherGeo.postValue(weatherCurrent.value)

                    }
                    //http://api.openweathermap.org/geo/1.0/direct/geo?q=istanbul&limit=12&appid=042c2ddfb3f95fa918336e6edbd4fe63}
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