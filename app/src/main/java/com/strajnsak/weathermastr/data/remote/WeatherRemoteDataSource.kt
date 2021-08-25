package com.strajnsak.weathermastr.data.remote

import com.strajnsak.weathermastr.data.entities.ArsoData
import com.strajnsak.weathermastr.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val weatherApi: WeatherApi
): MasterRemoteDataSource() {
    suspend fun getLatestWeatherManually(): Resource<ArsoData>{
        return getResult { weatherApi.getWeather() }
    }
    //val latestWeather = getResult { weatherApi.getWeather() }
    private val refreshIntervalMs: Long = 30000
    val latestWeather: Flow<Resource<ArsoData>> = flow {
        while(true) {
            emit(Resource.loading())
            val result = getResult { weatherApi.getWeather() }
            emit(result)
            delay(refreshIntervalMs)
        }
    }
/*
    val latestWeatherForcedRefresh: Flow<Resource<ArsoData>> = flow {
        emit(Resource.loading())
        val result = getResult { weatherApi.getWeather() }
        emit(result)
    }*/
}