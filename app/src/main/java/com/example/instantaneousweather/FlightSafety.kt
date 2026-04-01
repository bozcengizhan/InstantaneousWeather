package com.example.instantaneousweather

import androidx.compose.ui.graphics.Color

enum class FlightSafety(
    val message: String,
    val color: Color,
    val cardColor: Color,
    val backgroundColor: Color
) {
    SAFE("UÇUŞA UYGUN", Color(0xFF2E7D32), Color(0xFFA2EEA7), Color(0xFFB7D3C7)),
    CAUTION("KISMEN GÜVENLİ", Color(0xFFE65100), Color(0xFFFFE0B2), Color(0xFFF5E6CC)),
    DANGEROUS("RİSKLİ HAVA", Color.Red, Color(0xFFEEA2A2), Color(0xFFD3B7B7))
}