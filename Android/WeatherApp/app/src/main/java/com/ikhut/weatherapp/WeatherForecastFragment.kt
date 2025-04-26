package com.ikhut.weatherapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import com.ikhut.weatherapp.databinding.FragmentWeatherForecastBinding

class WeatherForecastFragment : Fragment() {
    private var capital: String? = null
    private var _binding: FragmentWeatherForecastBinding? = null
    private val binding get() = _binding!!
    private lateinit var forecastAdapter: WeatherForecastAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentWeatherForecastBinding.inflate(inflater, container, false)

        forecastAdapter = WeatherForecastAdapter()
        binding.recyclerView.adapter = forecastAdapter
        binding.recyclerView.addItemDecoration(
            DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL)
        )

        setupFlagClickListeners()

        parentFragmentManager.setFragmentResultListener(
            "dailyWeatherUpdate", viewLifecycleOwner
        ) { _, bundle ->
            val newCity = bundle.getString("capital") ?: TBILISI
            loadForecastData(newCity)
        }

        loadForecastData(capital ?: TBILISI)

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
        parentFragmentManager.setFragmentResult("forecastUpdate", result)
        loadForecastData(city)
    }

    private fun loadForecastData(city: String) {
        binding.capital.text = city.uppercase()

        WeatherForecastApi.getForecast(
            cityName = city,
            callback = object : WeatherForecastApi.ForecastCallback {
                override fun onSuccess(forecastData: WeatherForecastResponse) {
                    activity?.runOnUiThread {
                        forecastAdapter.updateForecast(forecastData.list)

                        val firstForecast = forecastData.list.firstOrNull()
                        if (firstForecast != null) {
                            updateBackgroundForLocalTime(
                                firstForecast.dt, forecastData.city.timezone
                            )
                        }
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
        val hour = TimeUtils.getLocalHour(timestamp, timezoneOffset) - 1

        val backgroundColor = when (hour) {
            in 6..18 -> ContextCompat.getColor(requireContext(), R.color.day)
            else -> ContextCompat.getColor(requireContext(), R.color.night)
        }

        binding.root.setBackgroundColor(backgroundColor)
        (binding.root.parent as? View)?.setBackgroundColor(backgroundColor)
    }
}
