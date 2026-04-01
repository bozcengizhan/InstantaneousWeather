package com.example.instantaneousweather.model

data class WeatherResponse(
    val data: List<WeatherData>
)

data class WeatherData(
    val temp: Double,
    val wind_spd: Double,    // Rüzgar hızı (m/s)
    val wind_cdir_full: String, // Rüzgar yönü (Kuzey, vb.)
    val vis: Double,         // Görünürlük (KM)
    val rh: Double,          // Nem
    val clouds: Int,         // Bulut oranı
    val city_name: String,
    val app_temp: Double     // Hissedilen sıcaklık
)