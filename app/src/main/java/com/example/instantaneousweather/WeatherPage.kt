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
                        DroneDataCard("SICAKLIK", "${data.temp}°C", Modifier.weight(1f))
                        DroneDataCard("NEM", "%${data.rh.toInt()}", Modifier.weight(1f))
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        DroneDataCard("RÜZGAR HAMLESİ", "${String.format("%.1f", data.wind_gust_spd * 3.6)} km/h", Modifier.weight(1f))
                        DroneDataCard("BASINÇ", "${data.pres.toInt()} hPa", Modifier.weight(1f))
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        DroneDataCard("GÜN DOĞUMU", data.sunrise, Modifier.weight(1f))
                        DroneDataCard("GÜN BATIMI", data.sunset, Modifier.weight(1f))
                    }

                    Row(modifier = Modifier.fillMaxWidth()) {
                        DroneDataCard("BULUT ORANI", "%${data.clouds}", Modifier.weight(1f))
                        DroneDataCard("UV INDEX", "${data.uv.toInt()}", Modifier.weight(1f))
                    }



                    Text(
                        text = "Konum: ${data.city_name}",
                        modifier = Modifier.padding(top = 20.dp),
                        color = Color.DarkGray
                    )
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
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = value, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

