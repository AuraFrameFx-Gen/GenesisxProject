package dev.aurakai.auraframefx.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

// Neon WiFi Icon
@Composable
fun NeonWiFiIcon(modifier: Modifier = Modifier, color: Color = Color.Cyan) {
    Canvas(modifier.size(40.dp)) {
        drawArc(
            color = color,
            startAngle = 200f,
            sweepAngle = 140f,
            useCenter = false,
            topLeft = Offset.Zero,
            size = size,
            style = Stroke(width = 5f)
        )
        drawArc(
            color = color,
            startAngle = 215f,
            sweepAngle = 110f,
            useCenter = false,
            topLeft = Offset(8f, 8f),
            size = Size(24f, 24f),
            style = Stroke(width = 3f)
        )
        drawCircle(color = color, radius = 3f, center = Offset(size.width / 2, size.height * 0.75f))
    }
}

// Neon Battery Icon
@Composable
fun NeonBatteryIcon(modifier: Modifier = Modifier, color: Color = Color.Cyan) {
    Canvas(modifier.size(40.dp)) {
        drawRect(
            color,
            topLeft = Offset(4f, 10f),
            size = Size(28f, 16f),
            style = Stroke(width = 4f)
        )
        drawRect(color, topLeft = Offset(32f, 15f), size = Size(4f, 6f), style = Stroke(width = 2f))
    }
}

// Neon Bluetooth Icon
@Composable
fun NeonBluetoothIcon(modifier: Modifier = Modifier, color: Color = Color.Cyan) {
    Canvas(modifier.size(40.dp)) {
        val centerX = size.width / 2
        size.height / 2
        drawLine(color, Offset(centerX, 8f), Offset(centerX, 32f), strokeWidth = 4f)
        drawLine(color, Offset(centerX, 8f), Offset(centerX + 10f, 18f), strokeWidth = 4f)
        drawLine(color, Offset(centerX, 32f), Offset(centerX + 10f, 22f), strokeWidth = 4f)
        drawLine(color, Offset(centerX, 8f), Offset(centerX - 10f, 18f), strokeWidth = 4f)
        drawLine(color, Offset(centerX, 32f), Offset(centerX - 10f, 22f), strokeWidth = 4f)
    }
}

// Neon Notification Shade (rounded rectangle with wireframe)
@Composable
fun NeonNotificationShade(modifier: Modifier = Modifier, color: Color = Color.Cyan) {
    Canvas(modifier.size(320.dp, 120.dp)) {
        drawRoundRect(
            color,
            topLeft = Offset(0f, 0f),
            size = size,
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(28f, 28f),
            style = Stroke(width = 6f)
        )
        // Decorative lines
        drawLine(color, Offset(20f, 20f), Offset(100f, 20f), strokeWidth = 3f)
        drawLine(color, Offset(220f, 100f), Offset(300f, 100f), strokeWidth = 3f)
        drawCircle(
            color = color,
            radius = 8f,
            center = Offset(60f, 100f),
            style = Stroke(width = 3f)
        )
    }
}
