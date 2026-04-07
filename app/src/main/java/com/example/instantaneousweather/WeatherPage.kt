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
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.res.stringResource
import com.example.instantaneousweather.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherPage(viewModel: WeatherViewModel) {
    val state = viewModel.uiState.value
    val context = LocalContext.current

    val (safety, adviceMessage) = state.weatherData?.let {
        getFlightSafetyAnalysis(it)
    } ?: (FlightSafety.SAFE to R.string.loading_data)

    val pullToRefreshState = rememberPullToRefreshState()


    Scaffold(containerColor = safety.backgroundColor) { padding ->

        PullToRefreshBox(
            modifier = Modifier.fillMaxSize().padding(padding),
            state = pullToRefreshState,
            isRefreshing = state.isLoading,
            onRefresh = {
                viewModel.refreshData(context)
            }
        ) {
            when {
                state.isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = Color.Black)
                    }
                }
                state.errorMessage != null -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text(text = state.errorMessage, color = Color.Red)
                    }
                }
                state.weatherData != null -> {
                    val data = state.weatherData

                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(padding)
                            .padding(16.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Spacer(Modifier.weight(0.1f))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
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
                                    text = stringResource(id = safety.messageRes),
                                    color = safety.color,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 32.sp
                                )

                                Spacer(modifier = Modifier.height(8.dp))

                                Text(
                                    text = stringResource(id = adviceMessage),
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color.Black.copy(alpha = 0.7f),
                                    textAlign = TextAlign.Center
                                )
                            }
                        }

                        Spacer(Modifier.weight(0.3f))

                        ActiveCompass(azimuth = viewModel.azimuth.value)

                        Spacer(Modifier.weight(0.5f))

                        Row(modifier = Modifier.fillMaxWidth()) {
                            DroneDataCard(R.string.label_wind, "${String.format("%.1f", data.wind_spd * 3.6)} km/h", Modifier.weight(1f))
                            DroneDataCard(R.string.label_clouds, "%${data.clouds}", Modifier.weight(1f))
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            DroneDataCard(R.string.label_gust, "${String.format("%.1f", data.wind_gust_spd * 3.6)} km/h", Modifier.weight(1f))
                            DroneDataCard(R.string.label_vis, "${data.vis} km", Modifier.weight(1f))
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            DroneDataCard(R.string.label_direction, data.wind_cdir_full.capitalizeWords(), Modifier.weight(1f))
                            DroneDataCard(R.string.label_temp, "${data.temp}°C", Modifier.weight(1f))
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            DroneDataCard(R.string.label_pressure, "${data.pres.toInt()} hPa", Modifier.weight(1f))
                            DroneDataCard(R.string.label_uv, "${data.uv.toInt()}", Modifier.weight(1f))
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            DroneDataCard(R.string.label_humidity, "%${data.rh.toInt()}", Modifier.weight(1f))
                            DroneDataCard(R.string.label_aqi, "${data.aqs ?: "-"}", Modifier.weight(1f))
                        }

                        Row(modifier = Modifier.fillMaxWidth()) {
                            DroneDataCard(R.string.label_sunrise, data.sunrise, Modifier.weight(1f))
                            DroneDataCard(R.string.label_sunset, data.sunset, Modifier.weight(1f))
                        }

                        Spacer(Modifier.weight(0.6f))

                        Text(
                            text = "${data.city_name}",
                            color = Color.Gray,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.headlineSmall,
                            fontFamily = FontFamily.Serif
                        )

                        Spacer(Modifier.weight(0.06f))
                    }
                }
            }

        }


    }
}

fun String.capitalizeWords(): String =
    split(" ").joinToString(" ") { it.lowercase().replaceFirstChar { char -> char.uppercase() } }

@Composable
fun DroneDataCard(labelRes: Int, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .padding(5.dp)
            .border(3.dp, Color.Black, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Column(
            modifier = Modifier.padding(8.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(id = labelRes), // Yazı yerine ID'den çekiyoruz
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
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
            .border(2.dp, Color.Black, RoundedCornerShape(50.dp)),
        shape = RoundedCornerShape(50.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {

            Text(
                text = "N",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Serif,
                modifier = Modifier.align(Alignment.TopCenter).padding(top = 2.dp)
            )

            Icon(
                imageVector = Icons.Default.North,
                contentDescription = null,
                modifier = Modifier
                    .size(40.dp)
                    .rotate(-azimuth),
                tint = Color.Red
            )
        }
    }
}

