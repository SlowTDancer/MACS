package com.ikhut.weatherapp

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


object DailyWeatherApi {
    private val apiService: DailyWeatherApiService

    init {
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()

        apiService = retrofit.create(DailyWeatherApiService::class.java)
    }

    fun getCurrentWeather(
        cityName: String, callback: WeatherCallback
    ) {
        val call = apiService.getCurrentWeather(cityName, API_KEY)

        call.enqueue(object : Callback<DailWeatherResponse> {
            override fun onResponse(
                call: Call<DailWeatherResponse>, response: Response<DailWeatherResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Response body is empty")
                } else {
                    callback.onError("Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DailWeatherResponse>, t: Throwable) {
                callback.onError("Network failure: ${t.message}")
            }
        })
    }

    interface WeatherCallback {
        fun onSuccess(weatherData: DailWeatherResponse)
        fun onError(errorMessage: String)
    }
}

interface DailyWeatherApiService {
    @GET("weather")
    fun getCurrentWeather(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Call<DailWeatherResponse>
}

data class DailWeatherResponse(
    val weather: List<WeatherInfo>, val main: MainInfo, val dt: Long, val timezone: Int
)

data class WeatherInfo(
    val description: String, val icon: String
)

data class MainInfo(
    val temp: Double,
    @SerializedName("feels_like") val feelsLike: Double,
    val humidity: Int,
    val pressure: Int
)