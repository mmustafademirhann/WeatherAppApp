package com.example.vbchatgptweatherapp.presentation

import GeographyAdapter
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.vbchatgptweatherapp.data.modelModelModel.trdTry.GeographiaCityModels
import com.example.vbchatgptweatherapp.databinding.ActivityGeographyBinding
import com.example.vbchatgptweatherapp.domain.network.Response
import com.example.vbchatgptweatherapp.presentation.viewModel.GeoViewModel

class GeographyActivity : AppCompatActivity(), GeographyAdapter.OnItemClickListener {
    private lateinit var binding: ActivityGeographyBinding
    private val geographyAdapter by lazy { GeographyAdapter(this) }
    private val geoViewModel: GeoViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGeographyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = geographyAdapter

        geoViewModel.weatherCurrent.observe(this, Observer { response ->
            when (response) {
                is Response.LoadingState -> {
                    // Show loading indicator if needed
                }

                is Response.Success -> {
                    val cityList = response.data
                    val sublists = geoViewModel.getSublists(cityList!!)
                    geographyAdapter.updateGeoData(sublists.flatten()) // Flatten if needed
                }

                is Response.Error -> {
                    // Handle error, show error message
                    Log.e("GeographyActivity", "Error: ${response.message}")
                    //respons error mesajı burayada yaılsın
                    binding.textView.text = response.message
                }

                is Response.ErrorResponse -> {
                    // Handle error response, show error message
                    Log.e("GeographyActivity", "Error Response: ${response.message}")
                }
            }
        })
        binding.editTextName.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().trim()
                if (query.isNotEmpty()) {
                    geoViewModel.fetchGeoCurrent(query, 2)
                } else {
                    // Optionally clear the adapter if query is empty
                    geographyAdapter.updateGeoData(emptyList())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("GeographyActivity", "Before text changed: $s")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("GeographyActivity", "Text changing: $s")
            }
        })
    }

    override fun onItemClick(lat: Double?, lon: Double?, name: String?, isFromSearch: Boolean) {
        val intent = Intent(binding.root.context, MainActivity::class.java)
        intent.putExtra("lat", lat)
        intent.putExtra("lon", lon)
        intent.putExtra("name",name)
        intent.putExtra("isFromSearch",true)
        binding.root.context.startActivity(intent)
        finish()
    }
}
