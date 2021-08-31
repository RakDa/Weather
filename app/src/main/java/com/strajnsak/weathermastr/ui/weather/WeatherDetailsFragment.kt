package com.strajnsak.weathermastr.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.strajnsak.weathermastr.R
import com.strajnsak.weathermastr.data.entities.WeatherData
import com.strajnsak.weathermastr.databinding.FragmentWeatherDetailsBinding
import com.strajnsak.weathermastr.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect

@AndroidEntryPoint
class WeatherDetailsFragment : Fragment() {
    private var _binding: FragmentWeatherDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherDetailsViewModel by viewModels()

    companion object {
        const val SELECTED_LOCATION_KEY = "SELECTED_LOCATION_KEY"
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.getString(SELECTED_LOCATION_KEY)?.let {
            viewModel.selectedLocation = it
        }
        if (viewModel.selectedLocation != null) {
            setupFlowCollection(viewModel.selectedLocation!!)
        } else {
            findNavController().navigateUp()
        }
    }

    private fun setupFlowCollection(location: String) {
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getWeatherDataForLocation(location).collect {
                    when (it) {
                        is Resource.Loading -> showLoadingUi()
                        is Resource.Success -> showWeatherData(it.data)
                        is Resource.Error -> {
                            Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                            findNavController().navigateUp()
                        }
                    }
                }
            }
        }
    }

    private fun showLoadingUi() {
        binding.weatherDetailsProgressBar.visibility = View.VISIBLE
    }

    private fun showWeatherData(item: WeatherData) {
        binding.weatherDetailsProgressBar.visibility = View.GONE

        binding.weatherDetailsLocation.text = item.compositeTimeOfMeasurementLocation.location

        val temperature = "${item.temperature}${item.temperatureUnit}"
        binding.weatherDetailsTemperature.text = temperature

        binding.weatherDetailsTimeOfMeasurement.text = item.compositeTimeOfMeasurementLocation.timeOfMeasurement

        var trend = getString(R.string.temp_change_same)
        when {
            (item.trend == -1) -> trend = getString(R.string.temp_change_down)
            (item.trend == 1) -> trend = getString(R.string.temp_change_up)
        }
        binding.weatherDetailsTrend.text = trend

        if (item.weatherStateIconUrl != "") {
            Glide.with(binding.root)
                    .load(item.weatherStateIconUrl)
                    .into(binding.weatherDetailsIcon)
        } else {
            binding.weatherDetailsIcon.setImageDrawable(null)
        }

        if (item.windDirectionIconUrl != "") {
            Glide.with(binding.root)
                    .load(item.windDirectionIconUrl)
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}