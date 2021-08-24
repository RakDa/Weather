package com.strajnsak.weathermastr.data.remote

import com.strajnsak.weathermastr.data.entities.WeatherList
import com.strajnsak.weathermastr.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val weatherApi: WeatherApi
): MasterRemoteDataSource() {
    private val refreshIntervalMs: Long = 30000
    val latestWeather: Flow<Resource<WeatherList>> = flow {
        while(true) {
            emit(Resource.loading())
            val result = getResult { weatherApi.getWeather() }
            emit(result)
            delay(refreshIntervalMs)
        }
    }
}