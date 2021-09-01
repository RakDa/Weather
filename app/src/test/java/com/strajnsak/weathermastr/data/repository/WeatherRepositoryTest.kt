package com.strajnsak.weathermastr.data.repository

import com.strajnsak.weathermastr.data.entities.WeatherData
import com.strajnsak.weathermastr.data.local.WeatherDataDao
import com.strajnsak.weathermastr.data.remote.WeatherRemoteDataSource
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.mockito.kotlin.mock
import org.mockito.kotlin.whenever
import java.util.*

class WeatherRepositoryTest {

    @Test
    fun addPositiveTrendToNewWeatherDataTest(){
        val weatherRemoteDataSource: WeatherRemoteDataSource = mock()
        val weatherLocalDataSource: WeatherDataDao = mock()
        val weatherRepository = WeatherRepository(weatherRemoteDataSource, weatherLocalDataSource)

        val newWeatherData: WeatherData = mock()
        whenever(newWeatherData.compositeTimeOfMeasurementLocation.location).thenReturn("Maribor")
        whenever(newWeatherData.temperature).thenReturn(20)

        val oldWeatherData: WeatherData = mock()
        whenever(oldWeatherData.compositeTimeOfMeasurementLocation.location).thenReturn("Maribor")
        whenever(oldWeatherData.temperature).thenReturn(15)

        val newWeatherDataList: List<WeatherData> = listOf(newWeatherData)
        val oldWeatherDataList: List<WeatherData> = listOf(oldWeatherData)

        weatherRepository.addTrendToNewWeatherData(oldWeatherDataList, newWeatherDataList)

        assertEquals(1, newWeatherDataList[0].trend)

    }
}