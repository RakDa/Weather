package com.strajnsak.weathermastr.ui.weather_overview

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.strajnsak.weathermastr.R
import com.strajnsak.weathermastr.data.entities.WeatherData
import com.strajnsak.weathermastr.databinding.FragmentWeatherOverviewBinding
import com.strajnsak.weathermastr.ui.weather_overview.WeatherOverviewViewModel
import com.strajnsak.weathermastr.util.Resource
import kotlinx.coroutines.flow.collect

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [WeatherOverviewFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class WeatherOverviewFragment : Fragment() {
    private var _binding: FragmentWeatherOverviewBinding? = null
    private val binding get() = _binding!!
    private val viewModel: WeatherOverviewViewModel by viewModels()
    private lateinit var adapter: WeatherOverviewAdapter



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentWeatherOverviewBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFlowCollection()

    }

    private fun setupRecyclerView(){
        adapter = WeatherOverviewAdapter(this)
        binding.weatherOverviewRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.weatherOverviewRecyclerView.adapter = adapter
    }

    private fun setupFlowCollection(){
        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.arsoData.collect {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            var output = ""
                            for (weatherData in it.data?.weatherDataList!!) {
                                output += weatherData.location + "\n"
                            }
                            //findViewById<TextView>(R.id.test_text).text = output

                        }
                        Resource.Status.ERROR ->
                            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()

                        Resource.Status.LOADING ->
                            Toast.makeText(context, "loading", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onClickedWeatherLocation(weatherData: WeatherData) {
        findNavController().navigate(
            R.id.action_weatherOverviewFragment_to_weatherDetailsFragment,
            bundleOf("weatherData" to weatherData)
        )
    }

}