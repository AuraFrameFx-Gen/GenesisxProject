package dev.aurakai.auraframefx.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun NeonWireframeBorder(
    modifier: Modifier = Modifier,
    color: Color = Color.Cyan,
    stroke: Dp = 4.dp,
) {
    Canvas(modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height
        // Top border with circuit lines
        drawLine(color, Offset(0f, 0f), Offset(w, 0f), strokeWidth = stroke.toPx())
        drawLine(color, Offset(0f, 0f), Offset(0f, h), strokeWidth = stroke.toPx())
        drawLine(color, Offset(w, 0f), Offset(w, h), strokeWidth = stroke.toPx())
        drawLine(color, Offset(0f, h), Offset(w, h), strokeWidth = stroke.toPx())
        // Decorative: left circuit corner
        drawCircle(
            color = color,
            radius = 12f,
            center = Offset(0f, h * 0.9f),
            style = Stroke(width = stroke.toPx())
        )
        drawLine(
            color,
            Offset(0f, h * 0.9f),
            Offset(w * 0.12f, h * 0.9f),
            strokeWidth = stroke.toPx()
        )
        // Decorative: right circuit bar
        drawLine(
            color,
            Offset(w, h * 0.2f),
            Offset(w * 0.7f, h * 0.2f),
            strokeWidth = stroke.toPx()
        )
        // Decorative: bottom left angled lines
        drawLine(
            color,
            Offset(w * 0.04f, h),
            Offset(w * 0.13f, h * 0.85f),
            strokeWidth = stroke.toPx()
        )
        drawLine(
            color,
            Offset(w * 0.13f, h * 0.85f),
            Offset(w * 0.23f, h),
            strokeWidth = stroke.toPx()
        )
        // Decorative: bottom right ticks
        for (i in 0..4) {
            drawLine(
                color,
                Offset(w * 0.8f + i * 10f, h),
                Offset(w * 0.8f + i * 10f, h - 12f),
                strokeWidth = stroke.toPx()
            )
        }
    }
}
