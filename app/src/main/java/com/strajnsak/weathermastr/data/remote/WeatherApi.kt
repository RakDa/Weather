package com.strajnsak.weathermastr.data.remote

import com.strajnsak.weathermastr.data.entities.ArsoDataXML
import retrofit2.Response
import retrofit2.http.GET

interface WeatherApi {
    @GET("observation_si_latest.xml")
    suspend fun getWeather() : Response<ArsoDataXML>
}