package com.strajnsak.weathermastr.ui.weather

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.strajnsak.weathermastr.R
import com.strajnsak.weathermastr.data.entities.WeatherData
import com.strajnsak.weathermastr.databinding.ItemWeatherBinding

class WeatherOverviewAdapter(private val listener: WeatherOverviewListener) :
    ListAdapter<WeatherData, WeatherOverviewViewHolder>(WeatherDataDiffCallback()) {

    interface WeatherOverviewListener {
        fun onClickedWeatherLocation(weatherData: WeatherData)
    }

    fun setItems(weatherData: List<WeatherData>) {
        if (itemCount > 0) {
            for (i in 0 until itemCount) {
                val oldWeatherData = getItem(i)
                for (newWeatherData in weatherData) {
                    if (oldWeatherData.compositeTimeOfMeasurementLocation.location == newWeatherData.compositeTimeOfMeasurementLocation.location) {
                        when {
                            oldWeatherData.temperature > newWeatherData.temperature -> {
                                newWeatherData.trend = -1
                            }
                            oldWeatherData.temperature < newWeatherData.temperature -> {
                                newWeatherData.trend = 1
                            }
                            else -> {
                                newWeatherData.trend = 0
                            }
                        }
                        break
                    }
                }
            }
        }
        submitList(weatherData)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WeatherOverviewViewHolder {
        val binding: ItemWeatherBinding =
            ItemWeatherBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return WeatherOverviewViewHolder(binding, listener)
    }

    override fun onBindViewHolder(holder: WeatherOverviewViewHolder, position: Int) =
        holder.bind(getItem(position))

}

class WeatherOverviewViewHolder(
    private val itemBinding: ItemWeatherBinding,
    private val listener: WeatherOverviewAdapter.WeatherOverviewListener
) : RecyclerView.ViewHolder(itemBinding.root),
    View.OnClickListener {

    private lateinit var weatherData: WeatherData

    init {
        itemBinding.root.setOnClickListener(this)
    }

    fun bind(item: WeatherData) {
        val context = itemBinding.root.context
        this.weatherData = item
        itemBinding.weatherItemLocation.text = item.compositeTimeOfMeasurementLocation.location
        itemBinding.root.setBackgroundColor(context.getColor(R.color.weather_item_background_default))
        var average = ""
        if(item.averageLast30Minutes != null) {
            average = "${context.getString(R.string.average_30_minute)} ${item.averageLast30Minutes}${item.temperatureUnit}"
            if(item.averageLast30Minutes!! > 25) {
                itemBinding.root.setBackgroundColor(context.getColor(R.color.weather_item_background_highlighted))
            }
        }
        itemBinding.weatherItem30minuteAverage.text = average
        val temperature = "${item.temperature}${item.temperatureUnit}"
        itemBinding.weatherItemTemperature.text = temperature
        var trend = context.getString(R.string.temp_change_same)
        when {
            (item.trend == -1) -> trend = context.getString(R.string.temp_change_up)
            (item.trend == 1) -> trend = context.getString(R.string.temp_change_down)
        }
        itemBinding.weatherItemTemperatureUpDown.text = trend
        itemBinding.weatherItemTimestamp.text =
            item.compositeTimeOfMeasurementLocation.timeOfMeasurement
        if (item.weatherStateIconUrl != "") {
            Glide.with(itemBinding.root)
                .load(item.weatherStateIconUrl)
                .into(itemBinding.weatherItemIcon)
        } else {
            itemBinding.weatherItemIcon.setImageDrawable(null)
        }

        if (item.windDirectionIconUrl != "") {
            Glide.with(itemBinding.root)
                .load(item.windDirectionIconUrl)
                .into(itemBinding.weatherItemWindDirectionIcon)
            val windspeedText = "${item.windSpeed}${item.windSpeedUnit}"
            itemBinding.weatherItemWindSpeed.text = windspeedText
            val windDirectionDegreesText = "${item.windDirectionDegrees}°"
            itemBinding.weatherItemWindDirection.text = windDirectionDegreesText
        } else {
            itemBinding.weatherItemWindDirectionIcon.setImageDrawable(null)
            itemBinding.weatherItemWindSpeed.text = ""
            itemBinding.weatherItemWindDirection.text = ""
        }
    }

    override fun onClick(v: View?) {
        listener.onClickedWeatherLocation(weatherData)
    }
}

class WeatherDataDiffCallback : DiffUtil.ItemCallback<WeatherData>() {
    override fun areItemsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
        return oldItem.compositeTimeOfMeasurementLocation.location == newItem.compositeTimeOfMeasurementLocation.location
    }

    override fun areContentsTheSame(oldItem: WeatherData, newItem: WeatherData): Boolean {
        return oldItem.compositeTimeOfMeasurementLocation.timeOfMeasurement == newItem.compositeTimeOfMeasurementLocation.timeOfMeasurement
                && oldItem.averageLast30Minutes == newItem.averageLast30Minutes
    }
}