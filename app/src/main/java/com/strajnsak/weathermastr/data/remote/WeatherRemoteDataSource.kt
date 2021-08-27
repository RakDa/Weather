package com.strajnsak.weathermastr.data.remote

import com.strajnsak.weathermastr.data.entities.ArsoDataXML
import com.strajnsak.weathermastr.data.entities.CompositeTimeOfMeasurementLocation
import com.strajnsak.weathermastr.data.entities.WeatherData
import com.strajnsak.weathermastr.util.Resource
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val weatherApi: WeatherApi
) : MasterRemoteDataSource() {

    suspend fun getLatestWeatherManually(): Resource<List<WeatherData>> {
        val result = getResult { weatherApi.getWeather() }
        if (result.status == Resource.Status.SUCCESS && result.data != null) {
            return Resource.success(covertArsoDataXMLtoWeatherDataList(result.data))
        }
        return Resource.error(result.message.toString())

    }

    private val refreshIntervalMs: Long = 30000
    val latestWeather: Flow<Resource<List<WeatherData>>> = flow {
        while (true) {
            emit(Resource.loading())
            val result = getResult { weatherApi.getWeather() }
            if (result.status == Resource.Status.SUCCESS && result.data != null) {
                emit(Resource.success(covertArsoDataXMLtoWeatherDataList(result.data)))
            } else if (result.status == Resource.Status.ERROR && result.message != null) {
                emit(Resource.error(result.message))
            }
            delay(refreshIntervalMs)
        }
    }


    private fun covertArsoDataXMLtoWeatherDataList(arsoData: ArsoDataXML): List<WeatherData> {
        val weatherDataList = ArrayList<WeatherData>()
        val systemCurrentTimeMilliseconds = System.currentTimeMillis()

        for (weatherDataXml in arsoData.weatherDataList) {
            var weatherStateIconUrl = ""
            if(weatherDataXml.weatherStateIcon != "") {
                weatherStateIconUrl = arsoData.iconUrlBase + weatherDataXml.weatherStateIcon + "." + arsoData.iconFormat
            }
            var windDirectionIconUrl = ""
            if(weatherDataXml.windDirectionIcon != "") {
                windDirectionIconUrl = arsoData.iconUrlBase + weatherDataXml.windDirectionIcon + "." + arsoData.iconFormat
            }
            weatherDataList.add(
                WeatherData(
                    CompositeTimeOfMeasurementLocation(
                        weatherDataXml.location,
                        weatherDataXml.timeOfMeasurement
                    ),
                    weatherStateIconUrl,
                    weatherDataXml.temperatureUnit,
                    weatherDataXml.temperature,
                    weatherDataXml.windDirectionDegrees,
                    weatherDataXml.windSpeedUnit,
                    windDirectionIconUrl,
                    weatherDataXml.windSpeed,
                    systemCurrentTimeMilliseconds,
                    0,
                    null
                )
            )
        }
        return weatherDataList;
    }
}