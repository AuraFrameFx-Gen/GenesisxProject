package dev.aurakai.auraframefx.ui.theme

import androidx.compose.ui.graphics.Color

val NeonTeal = Color(0xFF00FFF7)
val NeonPurple = Color(0xFFA259FF)
val NeonPink = Color(0xFFFF00E1)
val BackgroundBlack = Color.Black

val ColorScheme = darkColorScheme(
    primary = NeonTeal,
    secondary = NeonPurple,
    tertiary = NeonPink,
    background = BackgroundBlack,
    surface = BackgroundBlack,
    onPrimary = NeonPurple,
    onSecondary = NeonTeal,
    onBackground = NeonPurple,
    onSurface = NeonPurple
)
