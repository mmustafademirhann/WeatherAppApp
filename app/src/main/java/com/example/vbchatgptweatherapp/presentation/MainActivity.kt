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
import com.example.vbchatgptweatherapp.data.modelModelModel.WtWtWeather
import com.example.vbchatgptweatherapp.databinding.ActivityMainBinding
import com.example.vbchatgptweatherapp.domain.network.Response
import com.example.vbchatgptweatherapp.presentation.viewModel.WeatherViewModel
import com.example.vbchatgptweatherapp.repository.FunctionRepository


//gitte yüklendi

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    private var daysAdapter:DaysAdapter? = null
    //private val functionRepo=FunctionRepository()



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.daysRecyclerView.layoutManager=LinearLayoutManager(this)


        viewModel.weather.observe(this, Observer { weather ->
            if (weather is Response.Success){




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
                val weatherDataList = weather.data?.list ?: emptyList()

                // Group the weather data by day
                val groupedWeather = viewModel.groupWeatherByDay(weatherDataList)
                var daysAdapter = DaysAdapter(weather.data!!, groupedWeather) { clickedDay ->
                    daysAdapter?.updateExpandedDay(if (daysAdapter!!.expandedDay == clickedDay) null else{
                        clickedDay
                    })
                }
                daysAdapter = DaysAdapter(weather.data!!, groupedWeather) { clickedDay ->
                    val previousExpandedDay = daysAdapter?.expandedDay
                    daysAdapter?.updateExpandedDay(if (previousExpandedDay == clickedDay) null else clickedDay)

                    // Find the position of the clicked day and refresh it
                    val position = daysAdapter?.weatherItems?.indexOfFirst { it.date == clickedDay }
                    if (position != null && position != -1) {
                        daysAdapter?.notifyItemChanged(position)
                    }

                    // ... (refresh previouslyexpanded day if needed)
                }
                if (daysAdapter==null) {
                    // Initialize the DaysAdapter if it hasn't been initialized yet
                    daysAdapter =DaysAdapter(weather.data!!, groupedWeather) { clickedDay ->
                        val previousExpandedDay = daysAdapter.expandedDay
                        // Update the expanded day in the adapter
                        daysAdapter.updateExpandedDay(if (previousExpandedDay == clickedDay) null else clickedDay)

                        // Find theposition of the clicked day and refresh it
                        val position = daysAdapter.weatherItems.indexOfFirst { it.date == clickedDay }
                        if (position != -1) {
                            daysAdapter.notifyItemChanged(position) // Notify the adapter about the change
                        }

                        // If a previous day was expanded, find its position and refresh it as well
                        if (previousExpandedDay != null) {
                            val previousPosition = daysAdapter.weatherItems.indexOfFirst { it.date == previousExpandedDay }
                            if (previousPosition != -1) {
                                daysAdapter.notifyItemChanged(previousPosition) // Notify the adapter about the change
                            }
                        }
                    }
                    // Set the adapter and layout manager for the RecyclerView
                    binding.daysRecyclerView.adapter = daysAdapter
                    binding.daysRecyclerView.layoutManager = LinearLayoutManager(this)
                } else {
                    // If the adapter is already initialized, just update its data
                    daysAdapter.updateWeatherData(weather.data!!, groupedWeather)
                }
                binding.daysRecyclerView.adapter=daysAdapter


                // Setting the adapter with the grouped data

                //binding.daysRecyclerView.adapter = DaysAdapter(groupedWeather)
                binding.daysRecyclerView.layoutManager = LinearLayoutManager(this)


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