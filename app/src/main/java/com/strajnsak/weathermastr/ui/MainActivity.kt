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
import com.strajnsak.weathermastr.databinding.ActivityMainBinding
import com.strajnsak.weathermastr.ui.weather_overview.WeatherOverviewViewModel
import com.strajnsak.weathermastr.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        /*
        findViewById<Button>(R.id.reload_button).setOnClickListener {
            viewModel.forceRefreshData()
        }*/

    }
}