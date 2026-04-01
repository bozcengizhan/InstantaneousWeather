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

@Composable
fun WeatherPage() {
    // Tek sayfa kuralı: Eğer veriler ekrana sığmazsa aşağı kaydırılabilsin diye scroll ekledik.
    val scrollState = rememberScrollState()

    Scaffold(
        containerColor = Color(0xFFE0E0E0) // Biraz daha profesyonel bir gri
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1. ANA DURUM KARTI (Uçuşa Uygunluk)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp)
                    .border(3.dp, Color.Black),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = "UÇUŞ GÜVENLİĞİ", fontSize = 14.sp, color = Color.Gray)
                    Text(
                        text = "UÇUŞA UYGUN",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = Color(0xFF2E7D32) // Yeşil tonu
                    )
                }
            }

            // 2. RÜZGAR VE GÖRÜNÜRLÜK (Drone için hayati)
            Row(modifier = Modifier.fillMaxWidth()) {
                DroneDataCard("RÜZGAR", "12 km/h", Modifier.weight(1f))
                DroneDataCard("GÖRÜNÜRLÜK", "10+ km", Modifier.weight(1f))
            }

            // 3. SICAKLIK VE NEM
            Row(modifier = Modifier.fillMaxWidth()) {
                DroneDataCard("SICAKLIK", "24°C", Modifier.weight(1f))
                DroneDataCard("NEM", "%45", Modifier.weight(1f))
            }

            // 4. KP INDEX VE BULUT (Manyetik fırtına drone GPS'ini bozar)
            Row(modifier = Modifier.fillMaxWidth()) {
                DroneDataCard("KP INDEX", "2 (Düşük)", Modifier.weight(1f))
                DroneDataCard("BULUT", "%20", Modifier.weight(1f))
            }

            // 5. RÜZGAR YÖNÜ (Geniş Kart)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .border(3.dp, Color.Black),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "RÜZGAR YÖNÜ", fontWeight = FontWeight.Bold)
                    Text(text = "Kuzey Batı (NW)", color = Color.DarkGray)
                }
            }

            // Alt bilgi (Kullanıcının konumunda olduğunu hatırlatır)
            Text(
                text = "Şu anki konumunuzdaki veriler gösteriliyor.",
                fontSize = 11.sp,
                color = Color.Gray,
                modifier = Modifier.padding(top = 16.dp)
            )
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

@Preview(showBackground = true)
@Composable
fun WeatherPagePreview() {
    WeatherPage()
}