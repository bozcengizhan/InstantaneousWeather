package com.example.instantaneousweather

import WeatherPage
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.example.instantaneousweather.ui.theme.InstantaneousWeatherTheme
import com.example.instantaneousweather.viewmodels.WeatherViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // ViewModel'i oluşturuyoruz
        val viewModel = WeatherViewModel()

        // Uygulama açılır açılmaz veriyi çek (Örn: İstanbul koordinatları)
        viewModel.fetchWeatherData(41.0082, 28.9784)

        setContent {
            InstantaneousWeatherTheme {
                // WeatherPage'i çağırıyoruz ve viewModel'i gönderiyoruz
                WeatherPage(viewModel = viewModel)
            }
        }
    }
}
