package com.example.vbchatgptweatherapp.data.models

import com.google.gson.annotations.SerializedName

data class Clouds(
    @SerializedName("all")
    val all: Int?
)