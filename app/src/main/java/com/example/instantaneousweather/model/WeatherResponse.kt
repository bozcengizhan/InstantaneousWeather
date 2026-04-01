package com.example.instantaneousweather.model

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
    val aqs: Int?,              // Hava Kalitesi (Bazı paketlerde gelir)

    // ZAMANLAMA (Uçuş süresi planlama için)
    val sunrise: String,        // Gün doğumu (HH:mm formatında)
    val sunset: String,         // Gün batımı (HH:mm formatında)
    val timezone: String        // Bölge zaman dilimi
)