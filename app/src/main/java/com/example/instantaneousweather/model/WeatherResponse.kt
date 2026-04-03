package com.example.instantaneousweather.model

import com.example.instantaneousweather.FlightSafety

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

fun getFlightSafetyAnalysis(data: WeatherData): Pair<FlightSafety, String> {
    val windKmH = data.wind_spd * 3.6
    val gustKmH = data.wind_gust_spd * 3.6

    val pressureDiff = Math.abs(data.pres - 1013.25)

    return when {
        windKmH > 35 -> FlightSafety.DANGEROUS to "Çok Sert Rüzgar: Pervane ve Motor Hasarı Riski!"
        gustKmH > 45 -> FlightSafety.DANGEROUS to "Şiddetli Rüzgar Hamlesi: Cihaz Savrulabilir!"
        data.vis < 1.0 -> FlightSafety.DANGEROUS to "Görüş Yok: Uçuş Kesinlikle Yasak!"
        data.temp < -10 -> FlightSafety.DANGEROUS to "Aşırı Soğuk: Batarya Hücreleri Donabilir!"
        data.temp > 45 -> FlightSafety.DANGEROUS to "Aşırı Sıcak: Motor ve ESC Aşırı Isınabilir!"
        data.rh > 95 -> FlightSafety.DANGEROUS to "Çok Yüksek Nem: Elektronik Arıza veya Yağış Riski!"

        windKmH > 20 -> FlightSafety.CAUTION to "Sert Rüzgar: Batarya Tüketimi Artacaktır."
        gustKmH > 30 -> FlightSafety.CAUTION to "Değişken Rüzgar: Gimbal Sarsıntısı Olabilir."
        data.vis < 4.0 -> FlightSafety.CAUTION to "Düşük Görünürlük: Görüş Hattını wKaybetmeyin."
        data.clouds > 85 -> FlightSafety.CAUTION to "Yoğun Bulut: Sinyal Kalitesi ve Görüş Etkilenebilir."
        data.uv > 7 -> FlightSafety.CAUTION to "Yüksek UV: Kontrol Ekranı Isınabilir."
        pressureDiff > 20 -> FlightSafety.CAUTION to "Basınç Değişimi: Altimetre Hatalı Ölçebilir!"
        data.rh > 80 -> FlightSafety.CAUTION to "Yüksek Nem: Merceklerde Buğulanma Yapabilir."

        data.temp < 5 -> FlightSafety.SAFE to "Uçuşa Uygun Ancak: Bataryaları Isıtarak Kullanın."
        data.app_temp != data.temp -> FlightSafety.SAFE to "Uygun: Hissedilen Sıcaklık Farklı, Tedbirli Olun."

        else -> FlightSafety.SAFE to "Şartlar İdeal: Sorunsuz Bir Uçuş Dileriz!"
    }
}