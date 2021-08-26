package com.strajnsak.weathermastr.ui.weather_overview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
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
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class WeatherOverviewFragment : Fragment(), WeatherOverviewAdapter.WeatherOverviewListener {
    private var _binding: FragmentWeatherOverviewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherOverviewViewModel by activityViewModels()
    private lateinit var adapter: WeatherOverviewAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSwipeRefresh()
        setupFlowCollection()

    }


    private fun setupRecyclerView() {
        adapter = WeatherOverviewAdapter(this)
        binding.weatherOverviewRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.weatherOverviewRecyclerView.adapter = adapter
    }

    private fun setupSwipeRefresh() {
        binding.weatherOverviewSwipeRefresh.setOnRefreshListener {
            viewModel.forceRefreshData()

        }
    }

    private fun setupFlowCollection() {
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.arsoData.collect {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            it.data?.let { it1 -> adapter.setItems(it1) }
                            binding.weatherOverviewSwipeRefresh.isRefreshing = false
                        }
                        Resource.Status.ERROR -> {

                            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
                            binding.weatherOverviewSwipeRefresh.isRefreshing = false
                        }

                        Resource.Status.LOADING -> {
                            binding.weatherOverviewSwipeRefresh.isRefreshing = true
                        }
                    }
                }
            }
        }
    }

    override fun onClickedWeatherLocation(weatherData: WeatherData) {
        viewModel.selectedWeatherData = weatherData
        findNavController().navigate(
            R.id.action_weatherOverviewFragment_to_weatherDetailsFragment
        )
    }

}