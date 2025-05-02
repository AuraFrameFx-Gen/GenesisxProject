package dev.aurakai.auraframefx.ui.theme

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class NeonShapes// Neon Wireframe Border

@Composable
fun NeonWireframeBorder(
    modifier: Modifier = Modifier,
    color: Color,
    stroke: Dp = 2.dp,
) {
    Canvas(modifier = modifier.border(1.dp, color)) {
        val path = Path()
        val rectWidth = size.width
        val rectHeight = size.height
        path.moveTo(0f, 0f) // Top-left
        path.lineTo(rectWidth, 0f) // Top-right
        path.lineTo(rectWidth, rectHeight) // Bottom-right
        path.lineTo(0f, rectHeight) // Bottom-left
        path.close() // Close the path

        // Draw the path with neon effect
        drawPath(path, color, style = Stroke(width = stroke.toPx()))
    }
}