package com.strajnsak.weathermastr.data.entities

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "data")
data class ArsoData(
        @PropertyElement(name = "icon_url_base") val iconUrlBase: String?,
        @PropertyElement(name = "icon_format") val iconFormat: String?,
        @Element(name = "metData") val weatherDataList: List<WeatherData>?
)

@Xml(name = "metData")
data class WeatherData(
    @PropertyElement(name = "domain_longTitle") val location: String?,
    @PropertyElement(name = "tsValid_issued") val timeOfMeasurement: String?,
    @PropertyElement(name = "nn_icon-wwsyn_icon") val weatherStateIcon: String?,
    @PropertyElement(name = "t_var_unit") val temperatureUnit: String?,
    @PropertyElement(name = "t") val temperature: Int?,
    @PropertyElement(name = "dd_val") val windDirectionDegrees: Int?,
    @PropertyElement(name = "ff_var_unit") val windSpeedUnit: String?,
    @PropertyElement(name = "dd_icon") val windDirectionIcon: String?,
    @PropertyElement(name = "ff_val") val windSpeed: String?,
)
