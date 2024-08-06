package com.example.vbchatgptweatherapp.presentation.utils


fun Double?.celvinToCelcius():String{

    return this?.let { (it - 273.15).toInt().toString() } ?: "N/A"
}