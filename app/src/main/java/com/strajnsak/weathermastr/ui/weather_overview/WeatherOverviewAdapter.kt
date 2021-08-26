package com.strajnsak.weathermastr.ui.weather_overview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.strajnsak.weathermastr.data.entities.ArsoData
import com.strajnsak.weathermastr.data.entities.WeatherData
import com.strajnsak.weathermastr.databinding.ItemWeatherBinding

class WeatherOverviewAdapter(private val listener: WeatherOverviewListener) : ListAdapter<WeatherData, WeatherOverviewViewHolder>(WeatherDataDiffCallback()) {

    interface WeatherOverviewListener {
        fun onClickedWeatherLocation(weatherData: WeatherData)
    }

    private lateinit var iconUrlBase: String
    private lateinit var iconFormat: String

    fun setItems(arsoData: ArsoData) {
        this.iconUrlBase = arsoData.iconUrlBase.toString()
        this.iconFormat = arsoData.iconFormat.toString()
        submitList(arsoData.weatherDataList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherOverviewViewHolder {
        val binding: ItemWeatherBinding = ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherOverviewViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: WeatherOverviewViewHolder, position: Int) = holder.bind(getItem(position), iconUrlBase, iconFormat)
}

class WeatherOverviewViewHolder(private val itemBinding: ItemWeatherBinding, private val listener: WeatherOverviewAdapter.WeatherOverviewListener) : RecyclerView.ViewHolder(itemBinding.root),
        View.OnClickListener {

    private lateinit var weatherData: WeatherData

    init {
        itemBinding.root.setOnClickListener(this)
    }

    fun bind(item: WeatherData, iconUrlBase: String, iconFormat: String) {
        this.weatherData = item
        itemBinding.weatherItemLocation.text = item.location
        val temperature = "${item.temperature}${item.temperatureUnit}"
        itemBinding.weatherItemTemperature.text = temperature
        itemBinding.weatherItemTimestamp.text = item.timeOfMeasurement
        if(item.weatherStateIcon != "") {
            Glide.with(itemBinding.root)
                .load(iconUrlBase + item.weatherStateIcon + "." + iconFormat)
                .into(itemBinding.weatherItemIcon);
        } else {
            itemBinding.weatherItemIcon.setImageDrawable(null)
        }

        /*
        Glide.with(itemBinding.root)
                .load(item.image)
                .transform(CircleCrop())
                .into(itemBinding.image)*/
    }

    override fun onClick(v: View?) {
        listener.onClickedWeatherLocation(weatherData)
    }
}

class WeatherDataDiffCallback : DiffUtil.ItemCallback<WeatherData>() {
    override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
        return oldItem.location == newItem.location
    }

    override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
        return oldItem.timeOfMeasurement == newItem.timeOfMeasurement
    }
}