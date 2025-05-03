package dev.aurakai.auraframefx.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.aurakai.auraframefx.ui.theme.NeonPink
import dev.aurakai.auraframefx.ui.theme.NeonPurple

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeonWireframeCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = modifier
            .shadow(
                elevation = 8.dp,
                ambientColor = NeonPurple,
                spotColor = NeonPink
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black,
            contentColor = NeonPurple
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(
                        colors = listOf(
                            NeonPurple.copy(alpha = 0.2f),
                            NeonPink.copy(alpha = 0.2f)
                        )
                    ),
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            content()
        }
    }
}

@Composable
fun NeonWireframeButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        NeonPurple.copy(alpha = 0.2f),
                        NeonPink.copy(alpha = 0.2f)
                    )
                ),
                shape = RoundedCornerShape(8.dp)
            ),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color.Black,
            contentColor = NeonPurple
        )
    ) {
        Text(text = text)
    }
}

@Composable
fun NeonWireframeText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge,
) {
    Text(
        text = text,
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                color = NeonPurple.copy(alpha = 0.2f)
            )
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        NeonPurple.copy(alpha = 0.2f),
                        NeonPink.copy(alpha = 0.2f)
                    )
                )
            ),
        style = style
    )
}

@Composable
fun NeonWireframeDivider(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .height(1.dp)
            .fillMaxWidth()
            .border(
                width = 1.dp,
                brush = Brush.linearGradient(
                    colors = listOf(
                        NeonPurple.copy(alpha = 0.2f),
                        NeonPink.copy(alpha = 0.2f)
                    )
                )
            )
    )
}
