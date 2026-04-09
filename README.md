# 🚀 AirMate: Drone Safe Fly (InstantaneousWeather)

![Kotlin](https://img.shields.io/badge/Kotlin-1.9.0-blue.svg)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-2024.02.01-green.svg)
![Platform](https://img.shields.io/badge/Platform-Android-brightgreen.svg)

**AirMate** is an intelligent flight safety assistant designed for drone pilots. It doesn't just display weather data; it analyzes it. By calculating wind speed, gusts, visibility, and atmospheric pressure, AirMate provides real-time safety recommendations (**Safe, Caution, or Dangerous**) to ensure your equipment and flight remain secure.

---

## ✨ Key Features

* **🛡️ Smart Flight Analysis:** Real-time evaluation of weather conditions to provide instant flight safety advice based on aviation-grade parameters.
* **🌪️ Gust Tracking:** Monitors sudden wind gusts, the most critical factor for drone stability and motor health.
* **🧭 Integrated Active Compass:** Track your device's heading (azimuth) in real-time to maintain orientation during flight.
* **🌍 Dynamic Localization:** Automatically switches between **English** and **Turkish** based on device settings, including localized weather descriptions.
* **📡 Weatherbit API Integration:** High-precision meteorological data fetched globally.
* **☀️ Sunset/Sunrise Timing:** Plan your flights within legal daylight hours with precise solar timings for your current location.

---

## 🛠️ Tech Stack

* **Jetpack Compose:** Modern, declarative UI toolkit for a smooth and responsive user experience.
* **Retrofit & Gson:** Efficient API communication and JSON parsing for real-time weather updates.
* **Architecture:** Clean MVVM (Model-View-ViewModel) pattern for robust state management.
* **Material 3:** Modern design language optimized for high readability in outdoor and bright environments.
* **Android Sensors:** Direct integration with the device's magnetic field sensors for real-time compass functionality.

---

## 🚀 Getting Started

1.  **Clone the repository:**
    ```bash
    git clone [https://github.com/bozcengizhan/InstantaneousWeather.git](https://github.com/bozcengizhan/InstantaneousWeather.git)
    ```
2.  **API Key:** Obtain a free API key from [Weatherbit.io](https://www.weatherbit.io/) and add it to your project (e.g., in a `local.properties` or Constants file).
3.  **Build:** Open the project in Android Studio and sync Gradle.
4.  **Run:** Deploy to an Android device with a magnetometer (compass) sensor for the full experience.

---

## 📜 License

This project is licensed under the **MIT License**. See the [LICENSE](LICENSE) file for more details.

---

### 👨‍💻 Developer
**Cengizhan Boz**
* **GitHub:** [@bozcengizhan](https://github.com/bozcengizhan)
* **Play Store:** [AirMate: Drone Safe Fly](https://play.google.com/store/apps/details?id=com.bozcengizhan.instantaneousweather)

---
*Created with ❤️ for the drone community.*
