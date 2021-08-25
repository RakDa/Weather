package com.strajnsak.weathermastr.ui.weather_overview

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strajnsak.weathermastr.data.entities.ArsoData
import com.strajnsak.weathermastr.data.repository.WeatherRepository
import com.strajnsak.weathermastr.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherOverviewViewModel @Inject constructor (
    private val repository: WeatherRepository
    ) : ViewModel() {

    private val _arsoData = MutableStateFlow<Resource<ArsoData>>(Resource.loading())
    val arsoData: StateFlow<Resource<ArsoData>> = _arsoData

    init {
        viewModelScope.launch {
            repository.latestWeather.collect() { arsoData ->
                _arsoData.value = arsoData
            }
        }
    }

    fun forceRefreshData(){
        viewModelScope.launch {
            _arsoData.value = Resource.loading()
            _arsoData.value = repository.getLatestWeatherManually()
        }
    }

}