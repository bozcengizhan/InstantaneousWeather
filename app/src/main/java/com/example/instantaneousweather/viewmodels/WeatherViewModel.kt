package com.example.instantaneousweather.viewmodels

import android.content.Context
import android.location.Geocoder
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instantaneousweather.WeatherUiState
import com.example.instantaneousweather.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import java.util.Locale

class WeatherViewModel : ViewModel() {
    var uiState = mutableStateOf(WeatherUiState())
        private set

    private val API_KEY = "522c88e913f14f1b8fc4c892d9ae24c4"

    fun fetchWeatherData(context: Context, lat: Double, lon: Double) {
        viewModelScope.launch {
            uiState.value = uiState.value.copy(isLoading = true)

            // 1. İLÇE İSMİNİ BULMA (Reverse Geocoding)
            val districtName = try {
                val geocoder = Geocoder(context, Locale("tr"))
                // Koordinatları adrese çeviriyoruz
                val addresses = geocoder.getFromLocation(lat, lon, 1)
                // subAdminArea genelde ilçeyi, adminArea ili verir.
                addresses?.firstOrNull()?.subAdminArea ?: addresses?.firstOrNull()?.adminArea ?: "Bilinmeyen Konum"
            } catch (e: Exception) {
                "Konum Alınamadı"
            }

            try {
                val response = RetrofitClient.service.getCurrentWeather(
                    lat = lat,
                    lon = lon,
                    apiKey = API_KEY
                )

                val result = response.data.firstOrNull()

                // 2. API'den gelen veriyi bizim bulduğumuz ilçe ismiyle güncelliyoruz
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    weatherData = result?.copy(city_name = districtName), // İlçe ismini buraya bastık
                    errorMessage = null
                )
            } catch (e: Exception) {
                uiState.value = uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Veri hatası: ${e.message}"
                )
            }
        }
    }
}