package com.strajnsak.weathermastr.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.strajnsak.weathermastr.data.entities.WeatherData
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDataDao {
    @Query("SELECT * FROM weatherdata WHERE insertionTimeInMilliseconds = (SELECT MAX(insertionTimeInMilliseconds) FROM weatherdata)")
    suspend fun getLatestWeatherData(): List<WeatherData>

    @Query("SELECT AVG(temperature) FROM weatherdata WHERE location = :location AND insertionTimeInMilliseconds > :timestampMilliseconds")
    suspend fun getAverageTemperatureForLocationSinceTimestamp(location: String, timestampMilliseconds: Long): Int

    @Query("SELECT COUNT(*) FROM weatherdata WHERE location = :location AND insertionTimeInMilliseconds > :timestampMilliseconds")
    suspend fun countLocationDataCache(location: String, timestampMilliseconds: Long): Int

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(weatherData: List<WeatherData>)

}