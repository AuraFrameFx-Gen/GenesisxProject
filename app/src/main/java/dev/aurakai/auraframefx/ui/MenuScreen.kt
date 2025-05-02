package dev.aurakai.auraframefx.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MenuScreen(
    onNavigateToChat: () -> Unit,
    onNavigateToEcosystem: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            "AuraFrameFX",
            color = Color.Cyan,
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Button(
            onClick = onNavigateToChat,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Magenta),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Chat / Texting", color = Color.White)
        }
        Button(
            onClick = onNavigateToEcosystem,
            colors = ButtonDefaults.buttonColors(containerColor = Color.Cyan),
            modifier = Modifier.padding(vertical = 8.dp)
        ) {
            Text("Ecosystem", color = Color.White)
        }
    }
}
