package dev.aurakai.auraframefx.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import dev.aurakai.auraframefx.ui.theme.NeonPink
import dev.aurakai.auraframefx.ui.theme.NeonPurple
import dev.aurakai.auraframefx.ui.theme.NeonTeal

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NeonCard(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Card(
        modifier = modifier
            .shadow(elevation = 8.dp, ambientColor = NeonPurple, spotColor = NeonPink),
        colors = CardDefaults.cardColors(
            containerColor = Color.Black,
            contentColor = NeonPurple
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            NeonPurple.copy(alpha = 0.1f),
                            Color.Black
                        )
                    )
                )
        ) {
            content()
        }
    }
}

@Composable
fun NeonButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier,
        colors = ButtonDefaults.buttonColors(
            containerColor = NeonTeal,
            contentColor = Color.Black
        )
    ) {
        Text(text = text)
    }
}

@Composable
fun NeonText(
    text: String,
    modifier: Modifier = Modifier,
    style: TextStyle = MaterialTheme.typography.bodyLarge
) {
    Text(
        text = text,
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                color = NeonPink
            ),
        style = style
    )
}

@Composable
fun NeonDivider(
    modifier: Modifier = Modifier
) {
    Divider(
        modifier = modifier,
        color = NeonPurple.copy(alpha = 0.2f),
        thickness = 1.dp
    )
}

@Composable
fun NeonSwitch(
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Switch(
        checked = checked,
        onCheckedChange = onCheckedChange,
        modifier = modifier,
        colors = SwitchDefaults.colors(
            checkedThumbColor = NeonTeal,
            checkedTrackColor = NeonPurple.copy(alpha = 0.2f),
            uncheckedThumbColor = NeonPurple.copy(alpha = 0.5f),
            uncheckedTrackColor = NeonPurple.copy(alpha = 0.1f)
        )
    )
}
