package com.example.vbchatgptweatherapp.presentation

import DaysAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vbchatgptweatherapp.R
import com.example.vbchatgptweatherapp.databinding.ActivityMainBinding
import com.example.vbchatgptweatherapp.domain.network.Response
import com.example.vbchatgptweatherapp.presentation.viewModel.WeatherViewModel
import com.example.vbchatgptweatherapp.repository.FunctionRepository


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    //private val functionRepo=FunctionRepository()


    private val viewModel: WeatherViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.daysRecyclerView.layoutManager=LinearLayoutManager(this)


        viewModel.weather.observe(this, Observer { weather ->
            if (weather is Response.Success){
                val weatherDataList = weather.data?.list ?: emptyList()

                // Group the weather data by day
                val groupedWeather = viewModel.groupWeatherByDay(weatherDataList)
                val daysAdapter=DaysAdapter(groupedWeather) { weatherForDay ->
                    // Handle the click event, e.g., update another RecyclerView with hourly data
                    // You can use weatherForDay to get the hourly weather data for that specific day
                }
                binding.daysRecyclerView.adapter=daysAdapter


                // Set the adapter with the grouped data

                //binding.daysRecyclerView.adapter = DaysAdapter(groupedWeather)
                binding.daysRecyclerView.layoutManager = LinearLayoutManager(this)




                val weatherData = weather.data?.list?.firstOrNull()?.main
                val weatherDataString = weather.data?.list?.firstOrNull()?.weather?.firstOrNull()?.main
                binding.temperature.text = "${kelvinToCelsius(weatherData?.temp)}°"
                /*binding.temperature.text = "${((weather.data?.list?.firstOrNull()?.main?.temp?.toInt())?.minus(
                    (273)
                )).toString()}°"*/
                binding.cityName.text="${weather.data?.city?.name}"
                binding.tempMax.text = "↑ ${kelvinToCelsius(weatherData?.tempMax)}°"
                binding.tempMin.text = "↓ ${kelvinToCelsius(weatherData?.tempMin)}°"
                //var  cloud= weather.data?.list?.firstOrNull()?.clouds?.all

                //var snow=
                when(weatherDataString){
                    "Clouds"->{
                        binding.root.setBackgroundResource(R.drawable.cloudy_bg)
                        binding.weatherIcon.setImageResource(R.drawable.cloudy)
                    }

                    "Snow" ->{
                        binding.root.setBackgroundResource(R.drawable.snow_bg)
                        binding.weatherIcon.setImageResource(R.drawable.snowy)

                    }
                    "Rain" ->{
                        binding.root.setBackgroundResource(R.drawable.rainy_bg)
                        binding.weatherIcon.setImageResource(R.drawable.rainy)
                    }
                }


                /*if (functionRepo.isCloudy(weatherDataString)) {


                        binding.root.setBackgroundResource(R.drawable.cloudy_bg)

                }*/
            }else if(weather is Response.ErrorResponse){
                Toast.makeText(this,"error",Toast.LENGTH_SHORT)
            }



        })
        val city = binding.cityName.text.toString()
        if (city.isNotEmpty()) {
            viewModel.fetchWeather(city)
        }




    }
    fun kelvinToCelsius(kelvin: Double?): String {
        return kelvin?.let { (it - 273.15).toInt().toString() } ?: "N/A"
    }





}