package com.example.instantaneousweather

import WeatherPage
import android.Manifest
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import com.example.instantaneousweather.ui.theme.InstantaneousWeatherTheme
import com.example.instantaneousweather.viewmodels.WeatherViewModel
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class MainActivity : ComponentActivity() {

    // ViewModel'i burada tanımlıyoruz
    private val viewModel = WeatherViewModel()

    // KRİTİK DÜZELTME: İzin istemciyi onCreate dışına aldık.
    // Bu sayede uygulama henüz başlamadan kayıt işlemi tamamlanıyor ve çökme engelleniyor.
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        val fineLocationGranted = permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false)
        val coarseLocationGranted = permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false)

        if (fineLocationGranted || coarseLocationGranted) {
            // Herhangi birine izin verildiyse konumu al
            getUserLocation()
        } else {
            // İzin verilmediyse kullanıcıya hata mesajı gösterilebilir
            // viewModel.uiState.value = viewModel.uiState.value.copy(errorMessage = "Konum izni reddedildi.")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.startCompass(this)

        // Tam ekran deneyimi için
        enableEdgeToEdge()

        // Uygulama açıldığı anda izinleri fırlatıyoruz
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ))

        setContent {
            InstantaneousWeatherTheme {
                // WeatherPage artık ViewModel'e erişebiliyor
                WeatherPage(viewModel = viewModel)
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun getUserLocation() {
        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        // En son bilinen konumu almayı deniyoruz
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location ->
                if (location != null) {
                    // 'this' diyerek context'i de gönderiyoruz
                    viewModel.fetchWeatherData(this, location.latitude, location.longitude)
                }
            }
            .addOnFailureListener {
                // Konum alma işlemi tamamen başarısız olursa
            }
    }
}