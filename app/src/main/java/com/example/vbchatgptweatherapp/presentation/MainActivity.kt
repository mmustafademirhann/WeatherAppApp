package com.example.vbchatgptweatherapp.presentation

import DaysAdapter
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vbchatgptweatherapp.R
import com.example.vbchatgptweatherapp.RetrofitInstance
import com.example.vbchatgptweatherapp.data.modelModelModel.WtWtWeather
import com.example.vbchatgptweatherapp.databinding.ActivityMainBinding
import com.example.vbchatgptweatherapp.domain.network.Response
import com.example.vbchatgptweatherapp.presentation.viewModel.ConfigViewModel
import com.example.vbchatgptweatherapp.presentation.viewModel.CurrentViewModel
import com.example.vbchatgptweatherapp.presentation.viewModel.WeatherViewModel
import com.example.vbchatgptweatherapp.repository.FunctionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


//gitte yüklendi

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val viewModel: WeatherViewModel by viewModels()
    private val viewModel2:CurrentViewModel by viewModels()
    private var daysAdapter:DaysAdapter? = null
    private val viewModConfig by viewModels<ConfigViewModel>()
    private val LOCATION_PERMISSION_REQUEST_CODE = 1



    //private val functionRepo=FunctionRepository()


    //private var isThatShowed=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.daysRecyclerView.layoutManager=LinearLayoutManager(this)
        val topContent = findViewById<LinearLayout>(R.id.top_content)
        adjustTopContentMargin(topContent)

        binding.signalIcon.setOnClickListener{
            val intent = Intent(this, GeographyActivity::class.java)
            startActivity(intent)
        }





        viewModel2.weatherCurrent.observe(this, Observer { weatherCurrent ->

            when (weatherCurrent) {
                is Response.LoadingState ->{
                    hideWelcomeFragment()
                    if(!viewModConfig.isThatShowed){
                        showWelcomeFragment()
                        viewModConfig.isThatShowed=true
                    }else hideWelcomeFragment()


                }
                is Response.Success -> {

                    val weatherData = weatherCurrent.data?.main
                    //val weatherDataString = weatherCurrent.data?.list?.firstOrNull()?.weather?.firstOrNull()?.main
                    binding.temperature.text = "${kelvinToCelsius(weatherData?.temp)}°"
                }
                is Response.ErrorResponse -> {
                    hideWelcomeFragment()
                    Toast.makeText(this,"error",Toast.LENGTH_SHORT)
                }
                is Response.Error -> {
                    hideWelcomeFragment()
                    Toast.makeText(this,"error",Toast.LENGTH_SHORT)
                }

            }


        })








        viewModel.weather.observe(this, Observer { weather ->


            when (weather) {
                is Response.LoadingState ->hideWelcomeFragment()
                is Response.Success -> {


                    val weatherData = weather.data?.list?.firstOrNull()?.main
                    val weatherDataString = weather.data?.list?.firstOrNull()?.weather?.firstOrNull()?.main
                    //binding.temperature.text = "${kelvinToCelsius(weatherData?.temp)}°"
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
                            //binding.root.setBackgroundResource(R.drawable.cloudy_bg)
                            binding.bacgroundic.setImageResource(R.drawable.clody_wallpmine)
                            binding.weatherIcon.setImageResource(R.drawable.cloud_mnc)

                        }

                        "Snow" ->{
                            binding.bacgroundic.setImageResource(R.drawable.snow_minecraft)
                            binding.weatherIcon.setImageResource(R.drawable.snowy)


                            //binding.root.setBackgroundResource(R.drawable.snow_bg)
                            //binding.weatherIcon.setImageResource(R.drawable.snowy)

                        }
                        "Rain" ->{
                            binding.bacgroundic.setImageResource(R.drawable.rainy_minecraft)
                            binding.weatherIcon.setImageResource(R.drawable.rainy_img_mnc)

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

                    CoroutineScope(Dispatchers.Main).launch {
                        delay(6000) // Delay for 1 second (adjust as needed)
                        hideWelcomeFragment()

                    }



                }
                is Response.ErrorResponse -> {
                    hideWelcomeFragment()
                    Toast.makeText(this,"error",Toast.LENGTH_SHORT)
                }
                is Response.Error -> {
                    hideWelcomeFragment()
                    Toast.makeText(this,"error",Toast.LENGTH_SHORT)
                }

            }

        })
        val city = binding.cityName.text.toString()
        if (city.isNotEmpty()) {
            viewModel.fetchWeather(city,latitude = 0.0, longitude = 0.0)
            viewModel2.fetchWeatherCurrent(city, latitude = 0.0, longitude = 0.0)
        }

        binding.apply {
            var lat=intent.getDoubleExtra("lat",0.0)
            var lon=intent.getDoubleExtra("lon",0.0)
            var name=intent.getStringExtra("name")

            viewModel.fetchWeather(name.orEmpty(), latitude = lat, longitude = lon)
            viewModel2.fetchWeatherCurrent(name.orEmpty(), latitude = lat, longitude = lon)
        }

        checkLocationPermissions()

    }

    fun kelvinToCelsius(kelvin: Double?): String {
        return kelvin?.let { (it - 273.15).toInt().toString() } ?: "N/A"
    }

    override fun onConfigurationChanged(newConfig: Configuration) {super.onConfigurationChanged(newConfig)
        val topContent = findViewById<LinearLayout>(R.id.top_content)
        adjustTopContentMargin(topContent)


    }

    private fun adjustTopContentMargin(topContent: LinearLayout) {
        val params = topContent.layoutParams as ConstraintLayout.LayoutParams
        val margin = if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            140.dpToPx() // 4dp margin for portrait

        } else {

            24.dpToPx() // 100dp margin for landscape
        }
        params.topMargin = margin
        topContent.layoutParams = params
    }

    // Helper function to convert dp to pixels
    private fun Int.dpToPx(): Int {
        val scale = resources.displayMetrics.density
        return (this * scale + 0.5f).toInt()
    }
    private fun showWelcomeFragment() {
        CoroutineScope(Dispatchers.Main).launch {
            delay(60000) // Delay for 1 second (adjust as needed)
        }

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, WelcomeLoadFragment(), "fragment_welcome_load")
            .commit()

    }
    private fun hideWelcomeFragment() {

        supportFragmentManager.findFragmentByTag("fragment_welcome_load")?.let {
            supportFragmentManager.beginTransaction()
                .hide(it)
                .commit()
        }
    }
    private fun checkLocationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val fineLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            val coarseLocationPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)

            if (fineLocationPermission != PackageManager.PERMISSION_GRANTED ||
                coarseLocationPermission != PackageManager.PERMISSION_GRANTED) {

                // Request permissions if they are not granted
                ActivityCompat.requestPermissions(this,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION),
                    LOCATION_PERMISSION_REQUEST_CODE)
            } else {
                // Permissions are already granted
                onLocationPermissionsGranted()
            }
        } else {
            // Permissions are automatically granted for versions below Marshmallow
            onLocationPermissionsGranted()
        }
    }

    // Handle the result of the permission request
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permissions granted
                onLocationPermissionsGranted()
            } else {
                // Permissions denied
                Toast.makeText(this, "Location permissions are required to use this feature", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // This function will be called when the location permissions are granted
    private fun onLocationPermissionsGranted() {
        // Your code to handle location functionality here
    }


}
//