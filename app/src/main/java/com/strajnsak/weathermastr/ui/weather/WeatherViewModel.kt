package com.strajnsak.weathermastr.ui.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strajnsak.weathermastr.data.entities.WeatherData
import com.strajnsak.weathermastr.data.repository.WeatherRepository
import com.strajnsak.weathermastr.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val repository: WeatherRepository
) : ViewModel() {

    private val _arsoData = MutableStateFlow<Resource<List<WeatherData>>>(Resource.Loading)
    val arsoData: StateFlow<Resource<List<WeatherData>>> = _arsoData
    var selectedWeatherData: WeatherData? = null

    init {
        viewModelScope.launch {
            repository.latestWeather
                .collect() { arsoData ->
                    _arsoData.value = arsoData
                }
        }
    }

    fun getCachedData() {
        viewModelScope.launch {
            _arsoData.value = Resource.Loading
            _arsoData.value = repository.getCachedWeatherData()
        }
    }

    suspend fun getAverageTemperatureLast30minutesForLocation(location:String): Int {
        return repository.getAverageTemperatureLast30minutesForLocation(location)
    }

    suspend fun cacheForLocationLast30minutesExists(location:String): Boolean {
        return repository.cacheForLocationLast30minutesExists(location)
    }

    fun cacheWeatherData(weatherDataList: List<WeatherData>){
        viewModelScope.launch {
            repository.cacheWeatherData(weatherDataList)
        }
    }

    fun forceRefreshData() {
        viewModelScope.launch {
            _arsoData.value = Resource.Loading
            _arsoData.value = repository.getLatestWeatherManually()
        }
    }

}