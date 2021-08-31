package com.strajnsak.weathermastr.ui.weather

import androidx.lifecycle.ViewModel
import com.strajnsak.weathermastr.data.entities.WeatherData
import com.strajnsak.weathermastr.data.repository.WeatherRepository
import com.strajnsak.weathermastr.util.Resource
import com.strajnsak.weathermastr.util.ResultWrapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class WeatherOverviewViewModel @Inject constructor(
        private val repository: WeatherRepository
) : ViewModel() {

    suspend fun getRepeatingWeatherDataFlow(): Flow<Resource<List<WeatherData>>> {
        val refreshIntervalMs: Long = 30000
        return mapFlowData(
                flow {
                    while (true) {
                        emit(Resource.Loading)
                        when (val latestWeather = repository.getLatestWeatherList()) {
                            is ResultWrapper.Success -> emit(Resource.Success(latestWeather.data))
                            is ResultWrapper.Error -> emit(Resource.Error(latestWeather.message))
                        }
                        kotlinx.coroutines.delay(refreshIntervalMs)
                    }
                }
        )
    }

    suspend fun getForceRefreshWeatherDataFlow(): Flow<Resource<List<WeatherData>>> {
        return mapFlowData(
                flow {
                    emit(Resource.Loading)
                    when (val latestWeather = repository.getLatestWeatherList()) {
                        is ResultWrapper.Success -> emit(Resource.Success(latestWeather.data))
                        is ResultWrapper.Error -> emit(Resource.Error(latestWeather.message))
                    }
                }
        )
    }

    private fun mapFlowData(flow: Flow<Resource<List<WeatherData>>>): Flow<Resource<List<WeatherData>>> {
        return flow.map {
            if (it is Resource.Success) {
                for (weatherData in it.data) {
                    if (repository.cacheForLocationLast30minutesExists(weatherData.compositeTimeOfMeasurementLocation.location)) {
                        weatherData.averageLast30Minutes =
                                repository.getAverageTemperatureLast30minutesForLocation(
                                        weatherData.compositeTimeOfMeasurementLocation.location
                                )
                    }
                }
                Resource.Success(it.data)
            } else it
        }
    }

}