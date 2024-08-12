package com.example.vbchatgptweatherapp.presentation

import GeographyAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vbchatgptweatherapp.R
import com.example.vbchatgptweatherapp.RetrofitInstance
import com.example.vbchatgptweatherapp.data.modelModelModel.GeoModel
import com.example.vbchatgptweatherapp.data.modelModelModel.secndTry.GeoCityModel
import com.example.vbchatgptweatherapp.databinding.ActivityGeographyBinding
import com.example.vbchatgptweatherapp.databinding.ActivityMainBinding
import com.example.vbchatgptweatherapp.domain.network.Response
import com.example.vbchatgptweatherapp.presentation.viewModel.GeoViewModel

class GeographyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGeographyBinding
    private val viewModel: GeoViewModel by viewModels()
    private val geograpyhAdapter :GeographyAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityGeographyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.adapter = geograpyhAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)

        viewModel.cities.observe(this, Observer { cityList ->
            when (cityList) {
                is Response.LoadingState -> { /* Show loading indicator if needed */ }
                is Response.Success -> {
                    geograpyhAdapter?.updateGeoData(GeoCityModel())
                }
                is Response.Error -> { /* Handle error, show error message */ }
                is Response.ErrorResponse -> { /* Handle error response, show error message */ }
            }
        })
        binding.editTextName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString()
                viewModel.fetchGeoCurrent(query, 10)
                // Make an API call to fetch cities based on 'query'
                // Update the RecyclerView with the fetched cities
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("GeographyActivity", "Before text changed: $s") // Log before text change
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("GeographyActivity", "Text changing: $s") // Log as text is changing
            }
        })

    }
}