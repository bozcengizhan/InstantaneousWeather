package com.bozcengizhan.instantaneousweather

import WeatherPage
import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instantaneousweather.ui.theme.InstantaneousWeatherTheme
import com.bozcengizhan.instantaneousweather.viewmodels.WeatherViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class MainActivity : ComponentActivity() {

    private val viewModel = WeatherViewModel()

    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
        val coarseLocationGranted = permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)

        if (fineLocationGranted || coarseLocationGranted) {
            getUserLocation()
        } else {
            // Sadece "Konum izni reddedildi" yerine daha açıklayıcı ve Google'ı ikna edici bir metin:
            viewModel.uiState.value = viewModel.uiState.value.copy(
                isLoading = false,
                errorMessage = "AirMate requires location permission to show weather data and flight safety analysis. Please enable location in settings."
            )
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.startCompass(this)
        enableEdgeToEdge()

        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))

        setContent {
            InstantaneousWeatherTheme {
                WeatherPage(viewModel = viewModel)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    viewModel.fetchWeatherData(this, location.latitude, location.longitude)
                }
            }
            .addOnFailureListener {
                viewModel.uiState.value = viewModel.uiState.value.copy(
                    isLoading = false,
                    errorMessage = "Failed to get your location. Please check your GPS settings and try again."
                )
            }
    }
}