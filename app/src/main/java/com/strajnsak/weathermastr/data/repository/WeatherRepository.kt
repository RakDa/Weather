package com.strajnsak.weathermastr.data.repository

import com.strajnsak.weathermastr.data.entities.WeatherList
import com.strajnsak.weathermastr.data.remote.WeatherRemoteDataSource
import com.strajnsak.weathermastr.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource
) {
    val latestWeather: Flow<Resource<WeatherList>> = weatherRemoteDataSource.latestWeather
}

