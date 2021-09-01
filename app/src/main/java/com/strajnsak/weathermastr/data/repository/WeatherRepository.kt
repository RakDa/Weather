package com.strajnsak.weathermastr.data.repository

import androidx.annotation.VisibleForTesting
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
        val cachedWeatherData: List<WeatherData>? = let {
            when(val cache = getCachedWeatherData()) {
                is ResultWrapper.Success -> cache.data
                is ResultWrapper.Error -> null
            }
        }
        return when (val latestWeather = weatherRemoteDataSource.getLatestWeather()) {
            is ResultWrapper.Success -> {
                val newWeatherData: List<WeatherData> = latestWeather.data
                if(cachedWeatherData != null) {
                    addTrendToNewWeatherData(cachedWeatherData, newWeatherData)
                    addAverageTemperatureForPast30minutes(newWeatherData)
                }
                cacheWeatherData(newWeatherData)
                ResultWrapper.Success(newWeatherData)
            }
            is ResultWrapper.Error ->
                getCachedWeatherData()
        }
    }

    @VisibleForTesting
    public fun addTrendToNewWeatherData(oldWeatherDataList: List<WeatherData>, newWeatherDataList: List<WeatherData>){
        for (oldWeatherData in oldWeatherDataList) {
            for (newWeatherData in newWeatherDataList) {
                if (oldWeatherData.compositeTimeOfMeasurementLocation.location == newWeatherData.compositeTimeOfMeasurementLocation.location) {
                    when {
                        oldWeatherData.temperature > newWeatherData.temperature -> {
                            newWeatherData.trend = -1
                        }
                        oldWeatherData.temperature < newWeatherData.temperature -> {
                            newWeatherData.trend = 1
                        }
                        else -> {
                            newWeatherData.trend = 0
                        }
                    }
                    break
                }
            }
        }
    }

    private suspend fun addAverageTemperatureForPast30minutes(weatherDataList: List<WeatherData>){
        for (weatherData in weatherDataList) {
            if (cacheForLocationLast30minutesExists(weatherData.compositeTimeOfMeasurementLocation.location)) {
                weatherData.averageLast30Minutes = getAverageTemperatureLast30minutesForLocation(
                                weatherData.compositeTimeOfMeasurementLocation.location
                        )
            }
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

