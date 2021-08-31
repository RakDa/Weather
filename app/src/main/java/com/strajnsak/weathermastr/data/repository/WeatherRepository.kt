package com.strajnsak.weathermastr.data.repository

import com.strajnsak.weathermastr.data.entities.WeatherData
import com.strajnsak.weathermastr.data.local.WeatherDataDao
import com.strajnsak.weathermastr.data.remote.WeatherRemoteDataSource
import com.strajnsak.weathermastr.util.ResultWrapper
import javax.inject.Inject

class WeatherRepository @Inject constructor(
        private val weatherRemoteDataSource: WeatherRemoteDataSource,
        private val weatherLocalDataSource: WeatherDataDao
) {

    suspend fun getLatestWeatherList(): ResultWrapper<List<WeatherData>> {
        return when (val latestWeather = weatherRemoteDataSource.getLatestWeather()) {
            is ResultWrapper.Success -> {
                cacheWeatherData(latestWeather.data)
                latestWeather
            }
            is ResultWrapper.Error ->
                getCachedWeatherData()
        }
    }

    suspend fun getLatestWeatherForLocation(location: String): ResultWrapper<WeatherData> {
        val latestWeatherDataForLocation = weatherLocalDataSource.getLatestWeatherDataForLocation(location)
        return if(latestWeatherDataForLocation != null) {
            ResultWrapper.Success(latestWeatherDataForLocation)
        } else {
            ResultWrapper.Error("Local data not available")
        }
    }

    private suspend fun getCachedWeatherData(): ResultWrapper<List<WeatherData>> {
        val latestWeatherData = weatherLocalDataSource.getLatestWeatherData()
        return if (latestWeatherData.isNotEmpty()) {
            ResultWrapper.Success(latestWeatherData)
        } else {
            ResultWrapper.Error("Local data not available")
        }
    }

    private suspend fun cacheWeatherData(weatherDataList: List<WeatherData>) {
        weatherLocalDataSource.insertAll(weatherDataList)
    }

    suspend fun getAverageTemperatureLast30minutesForLocation(location: String): Int {
        val timestamp30minutesBack = System.currentTimeMillis() - (30 * 60000)
        return weatherLocalDataSource.getAverageTemperatureForLocationSinceTimestamp(location, timestamp30minutesBack)
    }

    suspend fun cacheForLocationLast30minutesExists(location: String): Boolean {
        val timestamp30minutesBack = System.currentTimeMillis() - (30 * 60000)
        return weatherLocalDataSource.countLocationDataCache(location, timestamp30minutesBack) > 0
    }


}

