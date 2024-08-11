package com.example.vbchatgptweatherapp.repository

import com.example.vbchatgptweatherapp.domain.network.WeatherApi

class GeograpyRepository(val api:WeatherApi) {
    fun getGeographies(q:String,limit:Int){
        api.getGeo(q,limit,"042c2ddfb3f95fa918336e6edbd4fe63")
    }


}