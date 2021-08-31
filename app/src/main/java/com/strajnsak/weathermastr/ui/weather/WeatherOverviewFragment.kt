package com.strajnsak.weathermastr.ui.weather

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.strajnsak.weathermastr.R
import com.strajnsak.weathermastr.data.entities.WeatherData
import com.strajnsak.weathermastr.databinding.FragmentWeatherOverviewBinding
import com.strajnsak.weathermastr.util.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WeatherOverviewFragment : Fragment(), WeatherOverviewAdapter.WeatherOverviewListener {
    private var _binding: FragmentWeatherOverviewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherOverviewViewModel by viewModels()
    private var adapter: WeatherOverviewAdapter? = null


    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipeRefresh()
        setupAutoRepeatingWeatherDataFlow()

    }

    private fun setupRecyclerView() {
        adapter = WeatherOverviewAdapter(this)
        binding.weatherOverviewRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.weatherOverviewRecyclerView.adapter = adapter
    }

    private fun setupSwipeRefresh() {
        binding.weatherOverviewSwipeRefresh.setOnRefreshListener {
            lifecycleScope.launch {
                collectFlow(viewModel.getForceRefreshWeatherDataFlow())
            }
        }
    }

    private fun setupAutoRepeatingWeatherDataFlow() {
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                collectFlow(viewModel.getRepeatingWeatherDataFlow())
            }
        }
    }

    private suspend fun collectFlow(flow: Flow<Resource<List<WeatherData>>>) {
        flow.collect {
            when (it) {
                is Resource.Success -> {
                    adapter?.setItems(it.data)
                    binding.weatherOverviewSwipeRefresh.isRefreshing = false
                }

                is Resource.Error -> {
                    Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                    binding.weatherOverviewSwipeRefresh.isRefreshing = false
                }

                is Resource.Loading -> {
                    binding.weatherOverviewSwipeRefresh.isRefreshing = true
                }
            }
        }
    }

    override fun onClickedWeatherLocation(weatherData: WeatherData) {
        findNavController().navigate(
                R.id.action_weatherOverviewFragment_to_weatherDetailsFragment,
                bundleOf(WeatherDetailsFragment.SELECTED_LOCATION_KEY to weatherData.compositeTimeOfMeasurementLocation.location)
        )
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        adapter = null
    }

}