package com.manigram.app.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val OledBlack = Color(0xFF000000)
val NeonPink = Color(0xFFFF007F)
val InstagramPink = Color(0xFFE1306C)
val SoftWhite = Color(0xFFF5F5F5)
val MutedGray = Color(0xFFA8A8A8)

private val ManigramColors: ColorScheme = darkColorScheme(
    primary = NeonPink, secondary = InstagramPink, background = OledBlack,
    surface = OledBlack, surfaceVariant = Color(0xFF171717),
    onPrimary = Color.White, onBackground = SoftWhite, onSurface = SoftWhite,
    onSurfaceVariant = MutedGray, error = Color(0xFFFF6B6B)
)

@Composable
fun ManigramTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = ManigramColors, content = content)
}
