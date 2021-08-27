package com.strajnsak.weathermastr.di

import android.content.Context
import androidx.room.Room
import com.strajnsak.weathermastr.data.local.AppDatabase
import com.strajnsak.weathermastr.data.local.WeatherDataDao
import com.strajnsak.weathermastr.data.remote.WeatherApi
import com.tickaroo.tikxml.TikXml
import com.tickaroo.tikxml.retrofit.TikXmlConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SingletonComponentModule {

    @Singleton
    @Provides
    fun provideRetrofit() : Retrofit = Retrofit.Builder()
        .baseUrl("https://meteo.arso.gov.si/uploads/probase/www/observ/surface/text/sl/")
        .addConverterFactory(
            TikXmlConverterFactory.create(
            TikXml.Builder()
                .exceptionOnUnreadXml(false)
                //.addTypeConverter(String.javaClass, HtmlEscapeStringConverter())
                .build()))
        .build()

    @Provides
    fun provideWeatherApi(retrofit: Retrofit): WeatherApi = retrofit.create(WeatherApi::class.java)

    @Singleton
    @Provides
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase = Room.databaseBuilder(
        appContext,
        AppDatabase::class.java,
        "weatherDB"
    ).build()

    @Provides
    fun provideWeatherDataDao(appDatabase: AppDatabase): WeatherDataDao {
        return appDatabase.weatherDataDao()
    }

}