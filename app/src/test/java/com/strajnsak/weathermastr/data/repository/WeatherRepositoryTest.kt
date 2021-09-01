package com.strajnsak.weathermastr.data.repository

import com.strajnsak.weathermastr.data.entities.CompositeTimeOfMeasurementLocation
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

        val newWeatherData = WeatherData(
                CompositeTimeOfMeasurementLocation("Maribor", ""),
                "",
                "",
                20,
                0,
                "",
                "",
                "",
                0,
                0,
                0
        )

        val oldWeatherData = WeatherData(
                CompositeTimeOfMeasurementLocation("Maribor", ""),
                "",
                "",
                15,
                0,
                "",
                "",
                "",
                0,
                0,
                0
        )

        val newWeatherDataList: List<WeatherData> = listOf(newWeatherData)
        val oldWeatherDataList: List<WeatherData> = listOf(oldWeatherData)

        weatherRepository.addTrendToNewWeatherData(oldWeatherDataList, newWeatherDataList)

        assertEquals(1, newWeatherDataList[0].trend)

    }
}