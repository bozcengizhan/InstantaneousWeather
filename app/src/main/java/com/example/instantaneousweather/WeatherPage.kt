import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.instantaneousweather.FlightSafety
import com.example.instantaneousweather.viewmodels.WeatherViewModel
import com.example.instantaneousweather.model.WeatherData
import com.example.instantaneousweather.WeatherUiState
import com.example.instantaneousweather.model.getFlightSafetyAnalysis
import com.example.instantaneousweather.ui.theme.InstantaneousWeatherTheme
import androidx.compose.ui.draw.rotate
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material.icons.filled.North
import androidx.compose.ui.text.font.FontFamily


@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    val state = viewModel.uiState.value

    // Analizi yapıyoruz
    val (safety, adviceMessage) = state.weatherData?.let {
        getFlightSafetyAnalysis(it)
    } ?: (FlightSafety.SAFE to "Veriler yükleniyor...")


    Scaffold(containerColor = safety.backgroundColor) { padding ->
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

                    Spacer(Modifier.weight(0.1f))

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .border(3.dp, Color.Black, RoundedCornerShape(16.dp)),
                        colors = CardDefaults.cardColors(containerColor = safety.cardColor),
                        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxWidth().padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = safety.message,
                                color = safety.color,
                                fontWeight = FontWeight.Bold,
                                fontSize = 32.sp
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = adviceMessage,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.Black.copy(alpha = 0.7f),
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    Spacer(Modifier.weight(0.3f))

                    ActiveCompass(azimuth = viewModel.azimuth.value)

                    Spacer(Modifier.weight(0.3f))

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

                    Row(modifier = Modifier.fillMaxWidth()) {
                        DroneDataCard("RÜZGAR YÖNÜ", "${data.wind_cdir_full}", Modifier.weight(1f))
                        DroneDataCard("Hava Kalitesi", "${data.aqs}", Modifier.weight(1f))
                    }

                    Spacer(Modifier.weight(0.6f))

                    Text(
                        text = "${data.city_name}",
                        color = Color.Gray,
                        style = MaterialTheme.typography.headlineSmall,
                        fontFamily = FontFamily.Serif
                    )

                    Spacer(Modifier.weight(0.05f))
                }
            }
        }
    }
}

@Composable
fun DroneDataCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(4.dp)
            .border(3.dp, Color.Black, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold, modifier = Modifier.align(Alignment.CenterHorizontally))
            Spacer(modifier = Modifier.height(1.dp))
            Text(text = value, fontSize = 16.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}


@Composable
fun ActiveCompass(azimuth: Float) {
    Card(
        modifier = Modifier
            .size(100.dp)
            .padding(4.dp)
            .border(2.dp, Color.Black, RoundedCornerShape(50.dp)), // Yuvarlak Pusula
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            // Kuzey İşareti (K)
            Text(
                text = "N",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 2.dp)
            )

            // Dönen Ok (Gerçek Kuzey'i gösterir)
            Icon(
                imageVector = Icons.Default.North,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .rotate(-azimuth), // Telefon döndükçe ok ters yöne dönerek Kuzey'i sabit tutar
                tint = Color.Red
            )
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
            temp = 32.4,
            app_temp = 21.0,
            wind_spd = 4.5,        // ~16.2 km/h
            wind_gust_spd = 8.2,   // ~29.5 km/h (Kritik veri!)
            wind_cdir_full = "Kuzey Batı",
            wind_dir = 315,
            vis = 13.0,
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
