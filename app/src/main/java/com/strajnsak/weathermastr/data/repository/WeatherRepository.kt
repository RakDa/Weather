package com.strajnsak.weathermastr.data.repository

import com.strajnsak.weathermastr.data.entities.ArsoData
import com.strajnsak.weathermastr.data.remote.WeatherRemoteDataSource
import com.strajnsak.weathermastr.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource
) {
    val latestWeather: Flow<Resource<ArsoData>> = weatherRemoteDataSource.latestWeather
    suspend fun getLatestWeatherManually():Resource<ArsoData> {
        return weatherRemoteDataSource.getLatestWeatherManually()
    }
}

