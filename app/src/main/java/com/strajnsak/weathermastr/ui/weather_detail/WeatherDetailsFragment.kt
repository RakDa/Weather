package com.strajnsak.weathermastr.ui.weather_detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.strajnsak.weathermastr.R
import com.strajnsak.weathermastr.databinding.FragmentWeatherDetailsBinding
import com.strajnsak.weathermastr.databinding.FragmentWeatherOverviewBinding
import com.strajnsak.weathermastr.ui.weather_overview.WeatherOverviewAdapter
import com.strajnsak.weathermastr.ui.weather_overview.WeatherOverviewViewModel

class WeatherDetailsFragment : Fragment() {
    private var _binding: FragmentWeatherDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherOverviewViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherDetailsBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    fun setupView(){
        val item = viewModel.selectedWeatherData
        if(item != null) {
            binding.weatherDetailsLocation.text = item.location
            val temperature = "${item.temperature}${item.temperatureUnit}"
            binding.weatherDetailsTemperature.text = temperature
            binding.weatherDetailsTimeOfMeasurement.text = item.timeOfMeasurement



        } else {
            findNavController().navigateUp()
        }
    }
}