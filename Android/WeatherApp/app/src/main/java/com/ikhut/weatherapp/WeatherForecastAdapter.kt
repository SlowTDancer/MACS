package com.ikhut.weatherapp

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ikhut.weatherapp.databinding.ItemWeatherForecastBinding
import java.util.Locale
import kotlin.math.roundToInt

class WeatherForecastAdapter : RecyclerView.Adapter<WeatherForecastAdapter.ForecastViewHolder>() {
    private var forecastList: List<ForecastItem> = emptyList()

    inner class ForecastViewHolder(
        private val binding: ItemWeatherForecastBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun bind(forecastItem: ForecastItem) {
            binding.dateTime.text = formatDateTime(forecastItem.dateTimeText)
            binding.temperature.text = "${forecastItem.main.temp.roundToInt()}°C"
            binding.description.text = forecastItem.weather[0].description
            Glide.with(binding.root).load(getIconURL(forecastItem.weather[0].icon))
                .into(binding.weatherIcon)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val binding = ItemWeatherForecastBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ForecastViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        holder.bind(forecastList[position])
    }

    override fun getItemCount(): Int = forecastList.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateForecast(newForecastList: List<ForecastItem>) {
        forecastList = newForecastList
        notifyDataSetChanged()
    }

    private fun getIconURL(iconCode: String): String {
        return "https://openweathermap.org/img/wn/${iconCode}@2x.png"
    }

    private fun formatDateTime(inputDateTime: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val outputFormat = SimpleDateFormat("hh a dd MMM", Locale.getDefault())

        val date = inputFormat.parse(inputDateTime)
        return outputFormat.format(date!!)
    }
}
