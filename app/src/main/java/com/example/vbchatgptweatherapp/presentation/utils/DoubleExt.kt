package com.example.vbchatgptweatherapp.presentation.utils

import androidx.recyclerview.widget.AsyncListDiffer
import com.example.vbchatgptweatherapp.data.modelModelModel.trdTry.GeographiaCityModels


fun Double?.celvinToCelcius():String{

    return this?.let { (it - 273.15).toInt().toString() } ?: "N/A"
}

