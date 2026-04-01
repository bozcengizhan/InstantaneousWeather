package com.example.instantaneousweather

import com.example.instantaneousweather.model.WeatherData

data class WeatherUiState(
    val isLoading: Boolean = false,
    val weatherData: WeatherData? = null,
    val errorMessage: String? = null
)