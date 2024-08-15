package com.example.vbchatgptweatherapp.presentation.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.vbchatgptweatherapp.RetrofitInstance
import com.example.vbchatgptweatherapp.data.modelModelModel.GeoModel
import com.example.vbchatgptweatherapp.data.modelModelModel.WeatherCurrent
import com.example.vbchatgptweatherapp.data.modelModelModel.trdTry.GeographiaCityModels
import com.example.vbchatgptweatherapp.domain.network.Response
import com.example.vbchatgptweatherapp.repository.GeograpyRepository
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException


class GeoViewModel(val repository: GeograpyRepository) : ViewModel() {

    constructor() : this(GeograpyRepository(RetrofitInstance.geoApi))

    private val _cities = MutableLiveData<Response<GeographiaCityModels>>()
    val cities: LiveData<Response<GeographiaCityModels>> get() = _cities


    suspend fun loadGegraphy(q: String, limit: Int) =
        repository.getGeographies(q, limit)

    private val _weatherGeo = MutableLiveData<Response<GeographiaCityModels>>()
    val weatherCurrent: LiveData<Response<GeographiaCityModels>>
        get() = _weatherGeo

    fun fetchGeoCurrent(query: String, limit: Int) {
    viewModelScope.launch {
        try {
            val response = RetrofitInstance.geoApi.getGeo(query, limit, RetrofitInstance.appid)
            if (response.isSuccessful) {
                response.body()?.let { data ->
                    _weatherGeo.postValue(Response.Success(data)) // Pass the original data
                }
            } else {
                _weatherGeo.postValue(Response.ErrorResponse(response.message().toString()))
            }
        } catch (e: IOException) {
            _weatherGeo.postValue(Response.Error(e.message ?: "IOException error"))
        } catch (e: HttpException) {
            _weatherGeo.postValue(Response.Error(e.message ?: "HttpException error"))
        } catch (e: Exception) {
            _weatherGeo.postValue(Response.Error(e.message ?: "Exception error"))
        }
    }
}



    fun getSublists(cityList: GeographiaCityModels): List<List<GeographiaCityModels.GeographiaCityModelsItem>> {
        // Example: Group items by a specific condition (e.g., city names starting with the same letter)
        val groupedCities =
            cityList.groupBy { it.name?.firstOrNull() } // Use safe call and default value for nulls
        return groupedCities.values.toList()
    }

}