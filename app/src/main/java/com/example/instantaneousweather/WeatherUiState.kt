package com.bozcengizhan.instantaneousweather

import com.bozcengizhan.instantaneousweather.model.WeatherData

data class WeatherUiState(
    val isLoading: Boolean = false,
    val weatherData: WeatherData? = null,
    val errorMessage: String? = null
)