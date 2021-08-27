package com.strajnsak.weathermastr.data.repository

import com.strajnsak.weathermastr.data.entities.WeatherData
import com.strajnsak.weathermastr.data.local.WeatherDataDao
import com.strajnsak.weathermastr.data.remote.WeatherRemoteDataSource
import com.strajnsak.weathermastr.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherRepository @Inject constructor(
    private val weatherRemoteDataSource: WeatherRemoteDataSource,
    private val weatherLocalDataSource: WeatherDataDao
) {
    val latestWeather: Flow<Resource<List<WeatherData>>> = weatherRemoteDataSource.latestWeather

    suspend fun getLatestWeatherManually():Resource<List<WeatherData>> {
        return weatherRemoteDataSource.getLatestWeatherManually()
    }

    suspend fun getCachedWeatherData():Resource<List<WeatherData>> {
        val latestWeatherData = weatherLocalDataSource.getLatestWeatherData()
        return if(latestWeatherData.isNotEmpty()) {
            Resource.success(latestWeatherData);
        } else {
            Resource.error("Local data not available")
        }
    }

    suspend fun cacheWeatherData(weatherDataList: List<WeatherData>){
        weatherLocalDataSource.insertAll(weatherDataList)
    }

    suspend fun getAverageTemperatureLast30minutesForLocation(location: String): Int{
        val timestamp30minutesBack = System.currentTimeMillis() - (30*60000)
        return weatherLocalDataSource.getAverageTemperatureForLocationSinceTimestamp(location, timestamp30minutesBack)
    }

    suspend fun cacheForLocationLast30minutesExists(location: String): Boolean {
        val timestamp30minutesBack = System.currentTimeMillis() - (30*60000)
        return weatherLocalDataSource.countLocationDataCache(location, timestamp30minutesBack) > 0
    }


}

