package com.strajnsak.weathermastr.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import com.strajnsak.weathermastr.R
import com.strajnsak.weathermastr.ui.weather_overview.WeatherOverviewViewModel
import com.strajnsak.weathermastr.util.Resource
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherOverviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.getWeather().observe(this, {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    var output = ""
                    for (weatherData in it.data?.weatherDataList!!){
                        output += weatherData.location+"\n"
                    }
                    findViewById<TextView>(R.id.test_text).text = output

                }
                Resource.Status.ERROR ->
                    Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()

                Resource.Status.LOADING ->
                    Toast.makeText(this, "loading", Toast.LENGTH_LONG).show()
            }

        })
    }
}