package com.aurakai.visualization

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.aurakai.auraframefx.ui.AICreationEngineScreen
import dev.aurakai.auraframefx.ui.AIFeaturesScreen
import kotlin.random.Random

// Top-level function (correctly placed)
fun randomInsight(): String {
    return listOf(
        "Your creativity is peaking! Time to try a wild theme.",
        "Aura says: 'Don't forget to sparkle today!'",
        "System is running smoother than a jazz solo.",
        "Try layering some overlays for extra magic!",
        "Kai is working on something clever behind the scenes..."
    ).random()
}

// Top-level @Composable function (correctly placed)
@Composable
fun CascadeVisionScreen() {
    var cpuUsage by remember { mutableFloatStateOf(Random.nextFloat() * 100) }
    var memoryUsage by remember { mutableFloatStateOf(Random.nextFloat() * 100) }
    var currentInsight by remember { mutableStateOf(randomInsight()) }
    val cpuUsageAnimated by animateFloatAsState(targetValue = cpuUsage, label = "cpu")
    val memoryUsageAnimated by animateFloatAsState(targetValue = memoryUsage, label = "mem")
    val cpuColorAnimated by animateColorAsState(
        targetValue = if (cpuUsageAnimated > 80) Color.Red else Color(0xFF536DFE),
        animationSpec = tween(durationMillis = 500),
        label = "cpuColorAnimation"
    )
    val memoryColorAnimated by animateColorAsState(
        targetValue = if (memoryUsage > 80) Color.Red else Color(0xFFFF8A65),
        animationSpec = tween(durationMillis = 500),
        label = "memColorAnimation"
    )
    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "\uD83D\uDC41️ CascadeVision Dashboard",
                style = MaterialTheme.typography.headlineLarge
            )
            Spacer(Modifier.width(16.dp))
            Icon(
                Icons.Filled.Info,
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier.size(32.dp)
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Filled.AutoAwesome,
                contentDescription = null,
                tint = Color.Yellow,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text("Realtime system monitoring:", style = MaterialTheme.typography.titleMedium)
        }
        Text(
            "CPU Usage: ${"%.1f".format(cpuUsageAnimated)}%",
            color = cpuColorAnimated,
            style = MaterialTheme.typography.titleLarge
        )
        Text(
            "Memory Usage: ${"%.1f".format(memoryUsageAnimated)}%",
            color = memoryColorAnimated,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(Modifier.height(8.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF80D8FF)),
                contentAlignment = Alignment.Center
            ) {
                Text("A", color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(Modifier.width(8.dp))
            Text("Aura Insight:", style = MaterialTheme.typography.titleMedium)
        }
        Card(
            modifier = Modifier
                .clickable { /* Could expand for more details or animate */ }
                .padding(8.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(Modifier.padding(8.dp)) {
                Text(currentInsight, color = Color.Magenta)
            }
        }
        Button(onClick = {
            cpuUsage = Random.nextFloat() * 100
            memoryUsage = Random.nextFloat() * 100
            currentInsight = randomInsight()
        }) {
            Text("✨ Get New Insight ✨")
        }
        Spacer(Modifier.height(24.dp))
        HorizontalDivider()
        Spacer(Modifier.height(16.dp))
        Text("AI Creation Engine", style = MaterialTheme.typography.titleLarge)
        AICreationEngineScreen(onBack = {})
        Spacer(Modifier.height(16.dp))
        Text("AI Features", style = MaterialTheme.typography.titleLarge)
        AIFeaturesScreen(onBack = {})
    }
}

