package com.ikhut.weatherapp

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.ikhut.weatherapp.databinding.FragmentDailyWeatherBinding
import kotlin.math.roundToInt

class DailyWeatherFragment : Fragment() {
    private var capital: String? = null
    private var _binding: FragmentDailyWeatherBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDailyWeatherBinding.inflate(inflater, container, false)

        ViewCompat.setOnApplyWindowInsetsListener(binding.dailyWeather) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0)
            insets
        }

        setupFlagClickListeners()

        parentFragmentManager.setFragmentResultListener(
            "forecastUpdate", viewLifecycleOwner
        ) { _, bundle ->
            val newCity = bundle.getString("capital") ?: TBILISI
            loadWeatherData(newCity)
        }

        loadWeatherData(capital ?: TBILISI)

        return binding.root
    }

    private fun setupFlagClickListeners() {
        binding.georgia.setOnClickListener { sendCityResult(TBILISI) }
        binding.uk.setOnClickListener { sendCityResult(LONDON) }
        binding.jamaica.setOnClickListener { sendCityResult(KINGSTON) }
    }

    private fun sendCityResult(city: String) {
        val result = Bundle().apply {
            putString("capital", city)
        }
        parentFragmentManager.setFragmentResult("dailyWeatherUpdate", result)
        loadWeatherData(city)
    }

    private fun loadWeatherData(city: String) {
        capital = city

        DailyWeatherApi.getCurrentWeather(cityName = city,
            callback = object : DailyWeatherApi.WeatherCallback {
                @SuppressLint("SetTextI18n")
                override fun onSuccess(weatherData: DailWeatherResponse) {
                    activity?.runOnUiThread {
                        binding.capital.text = city.uppercase()
                        Glide.with(requireContext()).load(getIconURL(weatherData.weather[0].icon))
                            .into(binding.weatherIcon)

                        binding.mainTemperature.text = "${weatherData.main.temp.roundToInt()}°C"
                        binding.temperatureValue.text = "${weatherData.main.temp.roundToInt()}°C"
                        binding.weatherDescription.text =
                            weatherData.weather[0].description.uppercase()
                        binding.feelsLikeValue.text = "${weatherData.main.feelsLike.roundToInt()}°C"
                        binding.humidityValue.text = "${weatherData.main.humidity}%"
                        binding.pressureValue.text = "${weatherData.main.pressure}"

                        updateBackgroundForLocalTime(weatherData.dt, weatherData.timezone)
                    }
                }

                override fun onError(errorMessage: String) {
                    activity?.runOnUiThread {
                        Toast.makeText(
                            requireContext(), "Error: $errorMessage", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            })
    }

    private fun updateBackgroundForLocalTime(timestamp: Long, timezoneOffset: Int) {
        val hour = TimeUtils.getLocalHour(timestamp, timezoneOffset)

        val backgroundColor = when (hour) {
            in 6..18 -> ContextCompat.getColor(requireContext(), R.color.day)
            else -> ContextCompat.getColor(requireContext(), R.color.night)
        }

        binding.root.setBackgroundColor(backgroundColor)
        (binding.root.parent as? View)?.setBackgroundColor(backgroundColor)
    }

    private fun getIconURL(iconCode: String): String {
        return "https://openweathermap.org/img/wn/${iconCode}@2x.png"
    }
}
