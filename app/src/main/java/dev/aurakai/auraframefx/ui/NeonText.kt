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
    color: Color = Color(0xFF00FFD0),
    glowColor: Color = Color(0xFF66FFF0),
    fontSize: TextUnit = 16.sp,
) {
    Text(
        text = text,
        modifier = modifier,
        fontSize = fontSize,
        style = TextStyle(
            color = color,
            shadow = Shadow(
                color = glowColor,
                blurRadius = 16f
            )
        )
    )
}
