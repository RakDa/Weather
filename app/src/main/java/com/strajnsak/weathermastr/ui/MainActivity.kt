package com.strajnsak.weathermastr.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.strajnsak.weathermastr.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

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