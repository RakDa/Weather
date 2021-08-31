package com.strajnsak.weathermastr.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherData(
        @PrimaryKey
        @Embedded
        val compositeTimeOfMeasurementLocation: CompositeTimeOfMeasurementLocation,
        val weatherStateIconUrl: String,
        val temperatureUnit: String,
        val temperature: Int,
        val windDirectionDegrees: Int,
        val windSpeedUnit: String,
        val windDirectionIconUrl: String,
        val windSpeed: String,
        val insertionTimeInMilliseconds: Long,
        var trend: Int, // -1 down, 0 same, 1 up
        var averageLast30Minutes: Int?
)


data class CompositeTimeOfMeasurementLocation(
        val location: String,
        val timeOfMeasurement: String,
)