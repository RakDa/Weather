package com.strajnsak.weathermastr.data.entities

import com.tickaroo.tikxml.annotation.Element
import com.tickaroo.tikxml.annotation.PropertyElement
import com.tickaroo.tikxml.annotation.Xml

@Xml(name = "data")
data class ArsoData(
        @PropertyElement(name = "credit") val credit: String?,
        @Element(name = "metData") val weatherDataList: List<WeatherData>?
)

@Xml(name = "metData")
data class WeatherData(
    @PropertyElement(name = "domain_longTitle") val location: String?
)
