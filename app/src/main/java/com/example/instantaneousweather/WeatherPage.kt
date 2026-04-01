import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instantaneousweather.viewmodels.WeatherViewModel
import com.example.instantaneousweather.model.WeatherData
import com.example.instantaneousweather.WeatherUiState
import com.example.instantaneousweather.ui.theme.InstantaneousWeatherTheme


@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    val state = viewModel.uiState.value

    Scaffold(containerColor = Color(0xFFE0E0E0)) { padding ->
        when {
            state.isLoading -> {
                // Yükleniyor ekranı
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Color.Black)
                }
            }
            state.errorMessage != null -> {
                // Hata mesajı
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.errorMessage, color = Color.Red)
                }
            }
            state.weatherData != null -> {
                // Gerçek Verilerle Panel
                val data = state.weatherData

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // UÇUŞ GÜVENLİĞİ (Rüzgar 20 km/h üstündeyse riskli diyelim)
                    val isSafe = data.wind_spd * 3.6 < 20 // m/s'yi km/h'ye çevirdik

                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp).border(3.dp, Color.Black)) {
                        Column(modifier = Modifier.padding(24.dp).align(Alignment.CenterHorizontally)) {
                            Text(text = "UÇUŞ GÜVENLİĞİ", fontSize = 14.sp)
                            Text(
                                text = if (isSafe) "UÇUŞA UYGUN" else "RİSKLİ HAVA",
                                color = if (isSafe) Color(0xFF2E7D32) else Color.Red,
                                fontWeight = FontWeight.Bold,
                                fontSize = 24.sp
                            )
                        }
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        DroneDataCard("RÜZGAR", "${String.format("%.1f", data.wind_spd * 3.6)} km/h", Modifier.weight(1f))
                        DroneDataCard("GÖRÜNÜRLÜK", "${data.vis} km", Modifier.weight(1f))
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        DroneDataCard("RÜZGAR HAMLESİ", "${String.format("%.1f", data.wind_gust_spd * 3.6)} km/h", Modifier.weight(1f))
                        DroneDataCard("BASINÇ", "${data.pres.toInt()} hPa", Modifier.weight(1f))
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        DroneDataCard("SICAKLIK", "${data.temp}°C", Modifier.weight(1f))
                        DroneDataCard("NEM", "%${data.rh.toInt()}", Modifier.weight(1f))
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        DroneDataCard("GÜN DOĞUMU", data.sunrise, Modifier.weight(1f))
                        DroneDataCard("GÜN BATIMI", data.sunset, Modifier.weight(1f))
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        DroneDataCard("BULUT ORANI", "%${data.clouds}", Modifier.weight(1f))
                        DroneDataCard("UV INDEX", "${data.uv.toInt()}", Modifier.weight(1f))
                    }

                    Spacer(Modifier.weight(0.5f))

                    Text(
                        text = "${data.city_name}",
                        color = Color.Gray,
                        style = MaterialTheme.typography.headlineSmall,
                    )

                    Spacer(Modifier.weight(0.1f))
                }
            }
        }
    }
}

@Composable
fun DroneDataCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(8.dp)
            .border(3.dp, Color.Black),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(
            modifier = Modifier.padding(12.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WeatherPagePreview() {
    // 1. Sahte bir ViewModel oluşturuyoruz
    val mockViewModel = WeatherViewModel()

    // 2. ViewModel'in içindeki state'i manuel olarak zengin verilerle dolduruyoruz
    mockViewModel.uiState.value = WeatherUiState(
        isLoading = false,
        weatherData = WeatherData(
            temp = 22.4,
            app_temp = 21.0,
            wind_spd = 4.5,        // ~16.2 km/h
            wind_gust_spd = 8.2,   // ~29.5 km/h (Kritik veri!)
            wind_cdir_full = "Kuzey Batı",
            wind_dir = 315,
            vis = 12.0,
            rh = 55.0,
            clouds = 15,
            pres = 1013.2,
            uv = 4.0,
            city_name = "Üsküdar / İstanbul",
            sunrise = "06:45",
            sunset = "19:32",
            timezone = "Europe/Istanbul",
            aqs = 42
        ),
        errorMessage = null
    )

    // 3. Kendi temanla sarmalayarak gösteriyoruz
    InstantaneousWeatherTheme {
        WeatherPage(viewModel = mockViewModel)
    }
}
