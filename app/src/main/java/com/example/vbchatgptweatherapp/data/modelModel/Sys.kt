package com.example.vbchatgptweatherapp.data.modelModel

import com.google.gson.annotations.SerializedName

data class Sys(
    @SerializedName("pod")
    val pod: String?
)