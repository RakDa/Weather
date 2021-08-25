package com.strajnsak.weathermastr.ui

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.strajnsak.weathermastr.R
import com.strajnsak.weathermastr.ui.weather_overview.WeatherOverviewViewModel
import com.strajnsak.weathermastr.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: WeatherOverviewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.arsoData.collect {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            var output = ""
                            for (weatherData in it.data?.weatherDataList!!) {
                                output += weatherData.location + "\n"
                            }
                            findViewById<TextView>(R.id.test_text).text = output

                        }
                        Resource.Status.ERROR ->
                            Toast.makeText(applicationContext, it.message, Toast.LENGTH_LONG).show()

                        Resource.Status.LOADING ->
                            Toast.makeText(applicationContext, "loading", Toast.LENGTH_LONG).show()
                    }

                }
            }
        }
        findViewById<Button>(R.id.reload_button).setOnClickListener {
            viewModel.forceRefreshData()
        }

    }
}