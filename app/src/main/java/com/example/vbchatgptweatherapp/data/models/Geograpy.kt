package com.example.vbchatgptweatherapp.data.models

import com.google.gson.annotations.SerializedName

data class Geograpy(
    @SerializedName("country")
    val country: String?,
    @SerializedName("id")
    val id: Int?,
    @SerializedName("sunrise")
    val sunrise: Int?,
    @SerializedName("sunset")
    val sunset: Int?,
    @SerializedName("type")
    val type: Int?
)