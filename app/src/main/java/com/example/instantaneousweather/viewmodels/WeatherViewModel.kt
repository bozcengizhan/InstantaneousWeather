package com.example.instantaneousweather.viewmodels

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class WeatherViewModel : ViewModel() {
    var uiState = mutableStateOf(WeatherUiState())
        private set

    private val API_KEY = "522c88e913f14f1b8fc4c892d9ae24c4"

    fun fetchWeatherData(lat: Double, lon: Double) {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(isLoading = true)
            try {
                val response = RetrofitClient.service.getCurrentWeather(
                    lat = lat,
                    lon = lon,
                    apiKey = API_KEY
                )

                // Weatherbit 'data' adında bir liste döndürür, ilk elemanı alıyoruz.
                val result = response.data.firstOrNull()
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    weatherData = result,
                    errorMessage = null
                )
            } catch (e: Exception) {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Veri çekilemedi: ${e.message}"
                )
            }
        }
    }
}