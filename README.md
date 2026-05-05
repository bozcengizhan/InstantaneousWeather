# 🚀 AirMate: Drone Safe Fly

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-2024.02.01-green.svg)
![Platform](https://img.shields.io/badge/Platform-Android-brightgreen.svg)
[![Google Play](https://img.shields.io/badge/Google_Play-Available_Now-success?logo=google-play)](https://play.google.com/store/apps/details?id=com.bozcengizhan.instantaneousweather)

**AirMate**, drone pilotları için geliştirilmiş akıllı bir uçuş güvenliği asistanıdır. Sadece hava durumu verilerini göstermekle kalmaz, bu verileri analiz ederek uçuşun güvenli olup olmadığını değerlendirir.

Rüzgar hızı, hamleler (gusts), görüş mesafesi ve atmosferik basınç gibi kritik parametreleri hesaplayan AirMate; uçuşunuz için gerçek zamanlı güvenlik önerileri (**Güvenli, Dikkatli veya Tehlikeli**) sunarak ekipmanınızı ve uçuşunuzu güvence altına alır.

<a href="https://play.google.com/store/apps/details?id=com.bozcengizhan.instantaneousweather">
  <img src="https://play.google.com/intl/en_us/badges/static/images/badges/en_badge_web_generic.png" width="200" alt="Get it on Google Play">
</a>

---

## ✨ Temel Özellikler

* **🛡️ Akıllı Uçuş Analizi:** Havacılık standartlarına dayalı parametrelerle anlık uçuş güvenliği tavsiyeleri.
* **🌪️ Rüzgar Hamlesi (Gust) Takibi:** Drone stabilitesi ve motor sağlığı için en kritik faktör olan ani rüzgar değişimlerini izleme.
* **🧭 Entegre Aktif Pusula:** Uçuş sırasında yönünüzü korumak için cihazın manyetik sensörlerini kullanan gerçek zamanlı pusula.
* **🌍 Dinamik Dil Desteği:** Cihaz ayarlarına göre otomatik olarak **Türkçe** veya **İngilizce** diline geçiş.
* **📡 Weatherbit API Entegrasyonu:** Küresel çapta yüksek hassasiyetli meteorolojik veriler.
* **☀️ Gün Doğumu/Batımı Zamanları:** Yasal uçuş saatlerini planlamanız için bulunduğunuz konuma özel güneş zamanlamaları.

---

## 🛠️ Teknik Altyapı

* **Jetpack Compose:** Modern ve deklaratif UI tasarımı.
* **Retrofit & Gson:** Weatherbit API ile verimli iletişim ve veri işleme.
* **Mimari:** Temiz kod ve sürdürülebilirlik için **MVVM (Model-View-ViewModel)** deseni.
* **Material 3:** Dış mekanlarda ve parlak ortamlarda yüksek okunabilirlik sağlayan tasarım dili.
* **Android Sensors:** Gerçek zamanlı pusula işlevi için manyetometre entegrasyonu.

---

## 🚀 Başlangıç

1.  **Projeyi Klonlayın:**
    ```bash
    git clone https://github.com/bozcengizhan/InstantaneousWeather.git
    ```
2.  **API Anahtarı:** [Weatherbit.io](https://www.weatherbit.io/) adresinden ücretsiz bir API anahtarı alın ve projeye ekleyin.
3.  **Derleme:** Android Studio'da projeyi açın ve Gradle dosyalarını senkronize edin.
4.  **Çalıştırma:** Pusula deneyimi için fiziksel bir Android cihazda test edin.

---

## 📜 Lisans

Bu proje **MIT Lisansı** ile lisanslanmıştır. Detaylar için [LICENSE](LICENSE) dosyasına göz atabilirsiniz.

---

### 👨‍💻 Geliştirici
**Cengizhan Boz**
* **GitHub:** [@bozcengizhan](https://github.com/bozcengizhan)
* **Play Store:** [AirMate: Drone Safe Fly](https://play.google.com/store/apps/details?id=com.bozcengizhan.instantaneousweather)

---
*Drone topluluğu için ❤️ ile oluşturuldu.*
