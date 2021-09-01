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
class WeatherDetailsViewModel @Inject constructor(
        private val repository: WeatherRepository
) : ViewModel() {

    var selectedLocation: String? = null

    suspend fun getWeatherDataForLocation(location: String): Flow<Resource<WeatherData>> {
        return flow {
            emit(Resource.Loading)
            when (val latestWeatherForLocation = repository.getLatestWeatherForLocation(location)) {
                is ResultWrapper.Success -> emit(Resource.Success(latestWeatherForLocation.data))
                is ResultWrapper.Error -> emit(Resource.Error(latestWeatherForLocation.message))
            }
        }
    }
}