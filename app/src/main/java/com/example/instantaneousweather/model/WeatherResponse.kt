package com.bozcengizhan.instantaneousweather.model

import com.bozcengizhan.instantaneousweather.FlightSafety
import com.bozcengizhan.instantaneousweather.R
import com.google.gson.annotations.SerializedName

data class WeatherResponse(
    val data: List<WeatherData>
)

data class WeatherData(
    val temp: Double,           // Sıcaklık (°C)
    val app_temp: Double,       // Hissedilen Sıcaklık (°C)
    val city_name: String,      // Şehir/Konum adı

    // RÜZGAR VERİLERİ (Drone için en kritik kısım)
    val wind_spd: Double,       // Ortalama Rüzgar hızı (m/s)
    val wind_gust_spd: Double,  // Rüzgar HAMLESİ / Ani darbe hızı (m/s)
    val wind_cdir_full: String, // Rüzgar yönü (Örn: Kuzey Doğu)
    val wind_dir: Int,          // Rüzgar açısı (0-360 derece - Pusula için lazım olur)

    // UÇUŞ ŞARTLARI
    val vis: Double,            // Görünürlük (KM)
    val rh: Double,             // Nem (%)
    val clouds: Int,            // Bulut oranı (%)
    val pres: Double,           // Basınç (mb - Altimetre kalibrasyonu için)
    val uv: Double,             // UV İndeksi (Güneş radyasyonu)

    @SerializedName("aqi") // JSON'dan 'aqi' olarak gelen veriyi 'aqs' değişkenine aktarır
    val aqs: Int?,

    // ZAMANLAMA (Uçuş süresi planlama için)
    val sunrise: String,        // Gün doğumu (HH:mm formatında)
    val sunset: String,         // Gün batımı (HH:mm formatında)
    val timezone: String        // Bölge zaman dilimi
)

fun getFlightSafetyAnalysis(data: WeatherData): Pair<FlightSafety, Int> {
    val windKmH = data.wind_spd * 3.6
    val gustKmH = data.wind_gust_spd * 3.6
    val pressureDiff = Math.abs(data.pres - 1013.25)

    return when {
        // --- DANGEROUS ---
        windKmH > 35 -> FlightSafety.DANGEROUS to R.string.analysis_wind_dangerous
        gustKmH > 45 -> FlightSafety.DANGEROUS to R.string.analysis_gust_dangerous
        data.vis < 1.0 -> FlightSafety.DANGEROUS to R.string.analysis_vis_dangerous
        data.temp < -10 -> FlightSafety.DANGEROUS to R.string.analysis_cold_dangerous
        data.temp > 45 -> FlightSafety.DANGEROUS to R.string.analysis_hot_dangerous
        data.rh > 95 -> FlightSafety.DANGEROUS to R.string.analysis_humidity_dangerous

        // --- CAUTION ---
        windKmH > 20 -> FlightSafety.CAUTION to R.string.analysis_wind_caution
        gustKmH > 30 -> FlightSafety.CAUTION to R.string.analysis_gust_caution
        data.vis < 4.0 -> FlightSafety.CAUTION to R.string.analysis_vis_caution
        data.clouds > 85 -> FlightSafety.CAUTION to R.string.analysis_clouds_caution
        data.uv > 7 -> FlightSafety.CAUTION to R.string.analysis_uv_caution
        pressureDiff > 20 -> FlightSafety.CAUTION to R.string.analysis_pressure_caution
        data.rh > 80 -> FlightSafety.CAUTION to R.string.analysis_humidity_caution

        // --- SAFE ---
        data.temp < 5 -> FlightSafety.SAFE to R.string.analysis_cold_safe
        data.app_temp != data.temp -> FlightSafety.SAFE to R.string.analysis_temp_diff_safe

        else -> FlightSafety.SAFE to R.string.analysis_ideal
    }
}