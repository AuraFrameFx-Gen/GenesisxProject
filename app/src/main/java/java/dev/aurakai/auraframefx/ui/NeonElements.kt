package dev.aurakai.auraframefx.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

// Neon Heart
@Composable
fun NeonHeart(modifier: Modifier = Modifier, color: Color = Color.Magenta) {
    Canvas(modifier.size(48.dp)) {
        // Draw a neon heart shape
        drawArc(
            color = color,
            startAngle = 135f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(12f, 12f),
            size = Size(24f, 24f),
            style = Stroke(width = 6f)
        )
        drawArc(
            color = color,
            startAngle = 225f,
            sweepAngle = 180f,
            useCenter = false,
            topLeft = Offset(0f, 12f),
            size = Size(24f, 24f),
            style = Stroke(width = 6f)
        )
        drawLine(
            color = color,
            start = Offset(12f, 36f),
            end = Offset(24f, 48f),
            strokeWidth = 6f
        )
        drawLine(
            color = color,
            start = Offset(24f, 48f),
            end = Offset(36f, 36f),
            strokeWidth = 6f
        )
    }
}

// Neon Hexagon
@Composable
fun NeonHexagon(modifier: Modifier = Modifier, color: Color = Color.Cyan) {
    Canvas(modifier.size(48.dp)) {
        val path = androidx.compose.ui.graphics.Path().apply {
            moveTo(24f, 0f)
            lineTo(48f, 12f)
            lineTo(48f, 36f)
            lineTo(24f, 48f)
            lineTo(0f, 36f)
            lineTo(0f, 12f)
            close()
        }
        drawPath(path, color, style = Stroke(width = 6f))
    }
}

// Neon Rectangle
@Composable
fun NeonRectangle(modifier: Modifier = Modifier, color: Color = Color.Magenta) {
    Canvas(modifier.size(48.dp)) {
        drawRect(color, style = Stroke(width = 6f))
    }
}

// Neon Circuit Corner
@Composable
fun NeonCircuitCorner(modifier: Modifier = Modifier, color: Color = Color.Cyan) {
    Canvas(modifier.size(32.dp)) {
        // L-shaped circuit line
        drawLine(color, Offset(0f, 0f), Offset(0f, 32f), strokeWidth = 4f)
        drawLine(color, Offset(0f, 32f), Offset(32f, 32f), strokeWidth = 4f)
        // Dots
        drawCircle(color = color, radius = 3f, center = Offset(0f, 0f))
        drawCircle(color = color, radius = 3f, center = Offset(0f, 32f))
        drawCircle(color = color, radius = 3f, center = Offset(32f, 32f))
    }
}

// Neon Circle
@Composable
fun NeonCircle(modifier: Modifier = Modifier, color: Color = Color.Cyan) {
    Canvas(modifier.size(36.dp)) {
        drawCircle(
            color = color,
            radius = size.minDimension / 2,
            center = Offset(size.width / 2, size.height / 2),
            style = Stroke(width = 6f)
        )
    }
}

// Neon Glow Text (for "LEVEL UP", etc.)
@Composable
fun NeonGlowText(text: String, color: Color, glowColor: Color, modifier: Modifier = Modifier) {
    Box(modifier) {
        Text(
            text = text,
            color = color,
            style = TextStyle(
                shadow = Shadow(
                    color = glowColor,
                    blurRadius = 16f
                )
            )
        )
    }
}