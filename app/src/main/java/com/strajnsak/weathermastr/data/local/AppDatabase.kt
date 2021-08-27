package com.strajnsak.weathermastr.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.strajnsak.weathermastr.data.entities.WeatherData

@Database(entities = [WeatherData::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun weatherDataDao(): WeatherDataDao
}