package com.strajnsak.weathermastr.ui.weather

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.strajnsak.weathermastr.databinding.FragmentWeatherDetailsBinding

class WeatherDetailsFragment : Fragment() {
    private var _binding: FragmentWeatherDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherViewModel by activityViewModels()

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

    private fun setupView(){
        val item = viewModel.selectedWeatherData
        if(item != null) {
            binding.weatherDetailsLocation.text = item.location
            val temperature = "${item.temperature}${item.temperatureUnit}"
            binding.weatherDetailsTemperature.text = temperature
            binding.weatherDetailsTimeOfMeasurement.text = item.timeOfMeasurement

            val iconUrlBase = viewModel.getIconUrlBase();
            val iconFormat = viewModel.getIconFormat();
            if(item.weatherStateIcon != "" && iconUrlBase != null && iconFormat != null) {
                Glide.with(binding.root)
                    .load(iconUrlBase + item.weatherStateIcon + "." + iconFormat)
                    .into(binding.weatherDetailsIcon)
            } else {
                binding.weatherDetailsIcon.setImageDrawable(null)
            }

            if(item.windDirectionIcon != "" && iconUrlBase != null && iconFormat != null){
                Glide.with(binding.root)
                    .load(iconUrlBase + item.windDirectionIcon + "." + iconFormat)
                    .into(binding.weatherDetailsWindDirectionIcon)
                val windspeedText = "${item.windSpeed}${item.windSpeedUnit}"
                binding.weatherDetailsWindSpeed.text = windspeedText
                val windDirectionDegreesText = "${item.windDirectionDegrees}°"
                binding.weatherDetailsWindDirection.text = windDirectionDegreesText
            } else {
                binding.weatherDetailsWindDirectionIcon.setImageDrawable(null)
                binding.weatherDetailsWindSpeed.text = ""
                binding.weatherDetailsWindDirection.text = ""

            }


        } else {
            findNavController().navigateUp()
        }
    }
}