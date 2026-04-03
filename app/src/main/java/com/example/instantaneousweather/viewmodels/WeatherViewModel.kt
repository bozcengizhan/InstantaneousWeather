package com.example.instantaneousweather.viewmodels

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.location.Geocoder
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.instantaneousweather.WeatherUiState
import com.example.instantaneousweather.retrofit.RetrofitClient
import kotlinx.coroutines.launch
import java.util.Locale

class WeatherViewModel : ViewModel(), SensorEventListener {

    private var lastLat: Double? = null
    private var lastLon: Double? = null
    var uiState = mutableStateOf(WeatherUiState())
        private set

    private val API_KEY = "522c88e913f14f1b8fc4c892d9ae24c4"

    fun fetchWeatherData(context: Context, lat: Double, lon: Double) {
        lastLat = lat
        lastLon = lon

        viewModelScope.launch {
            uiState.value = uiState.value.copy(isLoading = true)

            val districtName = try {
                val geocoder = Geocoder(context, Locale("tr"))
                val addresses = geocoder.getFromLocation(lat, lon, 1)
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

                uiState.value = uiState.value.copy(
                    isLoading = false,
                    weatherData = result?.copy(city_name = districtName),
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

    fun refreshData(context: Context) {
        val lat = lastLat
        val lon = lastLon

        if (lat != null && lon != null) {
            fetchWeatherData(context, lat, lon)
        } else {
        }
    }

    var azimuth = mutableStateOf(0f)

    private var sensorManager: SensorManager? = null
    private var accelerometer: Sensor? = null
    private var magnetometer: Sensor? = null

    private val gravity = FloatArray(3)
    private val geomagnetic = FloatArray(3)

    fun startCompass(context: Context) {
        sensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager?.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        magnetometer = sensorManager?.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD)

        sensorManager?.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI)
        sensorManager?.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (event == null) return

        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, gravity, 0, event.values.size)
        }
        if (event.sensor.type == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, geomagnetic, 0, event.values.size)
        }

        val R = FloatArray(9)
        val I = FloatArray(9)
        if (SensorManager.getRotationMatrix(R, I, gravity, geomagnetic)) {
            val orientation = FloatArray(3)
            SensorManager.getOrientation(R, orientation)
            azimuth.value = Math.toDegrees(orientation[0].toDouble()).toFloat()
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}