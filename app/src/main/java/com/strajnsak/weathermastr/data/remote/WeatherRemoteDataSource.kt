package com.strajnsak.weathermastr.data.remote

import com.strajnsak.weathermastr.data.entities.ArsoDataXML
import com.strajnsak.weathermastr.data.entities.CompositeTimeOfMeasurementLocation
import com.strajnsak.weathermastr.data.entities.WeatherData
import com.strajnsak.weathermastr.util.ResultWrapper
import javax.inject.Inject

class WeatherRemoteDataSource @Inject constructor(
    private val weatherApi: WeatherApi
) : MasterRemoteDataSource() {

    suspend fun getLatestWeather(): ResultWrapper<List<WeatherData>> {
        return when(val result = getResult { weatherApi.getWeather() }) {
            is ResultWrapper.Success ->
                ResultWrapper.Success(convertArsoDataXMLtoWeatherDataList(result.data))
            is ResultWrapper.Error ->
                ResultWrapper.Error(result.message)
        }
    }

    private fun convertArsoDataXMLtoWeatherDataList(arsoData: ArsoDataXML): List<WeatherData> {
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