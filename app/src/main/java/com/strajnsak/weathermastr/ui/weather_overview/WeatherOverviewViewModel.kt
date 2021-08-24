package com.strajnsak.weathermastr.ui.weather_overview

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.strajnsak.weathermastr.data.entities.ArsoData
import com.strajnsak.weathermastr.data.repository.WeatherRepository
import com.strajnsak.weathermastr.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherOverviewViewModel @Inject constructor (
    private val repository: WeatherRepository
    ) : ViewModel() {

    private val weatherListMutableLiveData: MutableLiveData<Resource<ArsoData>> by lazy {
        MutableLiveData<Resource<ArsoData>>()
    }

    fun getWeather(): LiveData<Resource<ArsoData>> {
        return weatherListMutableLiveData
    }

    init {
        viewModelScope.launch {
            // Trigger the flow and consume its elements using collect
            repository.latestWeather.collect() { latestWeatherResource ->
                weatherListMutableLiveData.postValue(latestWeatherResource)
                // Update View with the latest favorite news
            }
        }
    }


}