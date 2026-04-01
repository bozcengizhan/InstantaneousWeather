package com.example.instantaneousweather.retrofit

import com.example.instantaneousweather.model.WeatherResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface WeatherbitService {
    @GET("current")
    suspend fun getCurrentWeather(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("key") apiKey: String,
        @Query("lang") lang: String = "tr" // Verileri Türkçe almak için
    ): WeatherResponse
}