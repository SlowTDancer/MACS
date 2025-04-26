package com.ikhut.weatherapp

import com.google.gson.annotations.SerializedName
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

object WeatherForecastApi {
    private val apiService: WeatherForecastApiService

    init {
        val retrofit =
            Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
                .build()

        apiService = retrofit.create(WeatherForecastApiService::class.java)
    }

    fun getForecast(
        cityName: String, callback: ForecastCallback
    ) {
        val call = apiService.getForecast(cityName, API_KEY)

        call.enqueue(object : Callback<WeatherForecastResponse> {
            override fun onResponse(
                call: Call<WeatherForecastResponse>, response: Response<WeatherForecastResponse>
            ) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        callback.onSuccess(it)
                    } ?: callback.onError("Response body is empty")
                } else {
                    callback.onError("Error: ${response.code()} - ${response.message()}")
                }
            }

            override fun onFailure(call: Call<WeatherForecastResponse>, t: Throwable) {
                callback.onError("Network failure: ${t.message}")
            }
        })
    }

    interface ForecastCallback {
        fun onSuccess(forecastData: WeatherForecastResponse)
        fun onError(errorMessage: String)
    }
}

interface WeatherForecastApiService {
    @GET("forecast")
    fun getForecast(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String,
        @Query("units") units: String = "metric"
    ): Call<WeatherForecastResponse>
}

data class WeatherForecastResponse(
    val list: List<ForecastItem>, val city: CityInfo
)

data class ForecastItem(
    val dt: Long,
    val main: TemperatureInfo,
    val weather: List<WeatherInfo>,
    @SerializedName("dt_txt") val dateTimeText: String
)

data class TemperatureInfo(
    val temp: Double
)

data class CityInfo(
    val name: String, val timezone: Int
)