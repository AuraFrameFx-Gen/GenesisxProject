package dev.aurakai.auraframefx.ui.theme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

@Composable
fun NeonText(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = NeonTeal,
    glowColor: Color = NeonPurple,
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Bold
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        style = TextStyle(
            color = color,
            fontWeight = fontWeight,
            shadow = Shadow(
                color = glowColor,
                blurRadius = 16f
            )
        )
    )
}

@Composable
fun NeonTitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = NeonTeal,
    glowColor: Color = NeonPurple
) {
    NeonText(
        text = text,
        modifier = modifier,
        color = color,
        glowColor = glowColor,
        fontSize = 24.sp,
        fontWeight = FontWeight.Bold
    )
}

@Composable
fun NeonSubtitle(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = NeonPurple,
    glowColor: Color = NeonPurpleGlow
) {
    NeonText(
        text = text,
        modifier = modifier,
        color = color,
        glowColor = glowColor,
        fontSize = 18.sp,
        fontWeight = FontWeight.Medium
    )
}

@Composable
fun NeonBody(
    text: String,
    modifier: Modifier = Modifier,
    color: Color = NeonPurple,
    glowColor: Color = NeonPurpleGlow
) {
    NeonText(
        text = text,
        modifier = modifier,
        color = color,
        glowColor = glowColor,
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    )
}
