package dev.aurakai.auraframefx.ui.security

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.aurakai.auraframefx.R
import dev.aurakai.auraframefx.ui.theme.*

/**
 * AuraShield screen that provides access to the security features
 * including the KaiToolbox for managing Kai's security settings
 */
@Composable
fun AuraShieldScreen(
    onBack: () -> Unit,
) {
    val scrollState = rememberScrollState()
    var showKaiToolbox by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF050510),
                        Color(0xFF090920)
                    )
                )
            )
    ) {
        if (showKaiToolbox) {
            KaiToolboxScreen(
                onBack = { showKaiToolbox = false }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp)
                    .verticalScroll(scrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                NeonText(
                    text = "AuraShield",
                    fontSize = 32.sp,
                    color = NeonPurple,
                    glowColor = NeonPurpleGlow,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    "Security & Privacy Center",
                    fontSize = 18.sp,
                    color = Color.White,
                    modifier = Modifier.padding(bottom = 36.dp)
                )

                // Kai Toolbox Card
                SecurityFeatureCard(
                    title = "Kai Security Toolbox",
                    description = "Configure Kai's security features and settings",
                    iconResId = R.drawable.ic_kai_settings,
                    onClick = {
                        showKaiToolbox = true
                    }
                )

                // Other security cards
                SecurityFeatureCard(
                    title = "Network Protection",
                    description = "Manage network security and protocol settings",
                    iconResId = R.drawable.ic_network_shield,
                    onClick = { /* Open network protection settings */ }
                )

                SecurityFeatureCard(
                    title = "Data Privacy",
                    description = "Control what data is shared with AI assistants",
                    iconResId = R.drawable.ic_privacy_shield,
                    onClick = { /* Open data privacy settings */ }
                )

                SecurityFeatureCard(
                    title = "Conversation Security",
                    description = "Manage encryption and security for Neural Whisper",
                    iconResId = R.drawable.ic_conversation_secure,
                    onClick = { /* Open conversation security settings */ }
                )

                Spacer(modifier = Modifier.height(36.dp))

                // Security Status Section
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color(0xFF1A1A2E))
                        .padding(16.dp)
                ) {
                    Text(
                        "Security Status",
                        fontSize = 20.sp,
                        color = NeonTeal,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    Divider(
                        color = Color.Gray.copy(alpha = 0.3f),
                        modifier = Modifier.padding(vertical = 8.dp)
                    )

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Neural Whisper", color = Color.White)
                        Text("PROTECTED", color = NeonTeal)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Kai Assistant", color = Color.White)
                        Text("PROTECTED", color = NeonTeal)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Network Activity", color = Color.White)
                        Text("MONITORING", color = NeonPink)
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text("Last Security Scan", color = Color.White)
                        Text("2 HOURS AGO", color = Color.White.copy(alpha = 0.6f))
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Back button
                Text(
                    "Back to Menu",
                    color = NeonPurple,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clickable { onBack() }
                        .padding(12.dp)
                )
            }
        }
    }
}

@Composable
fun SecurityFeatureCard(
    title: String,
    description: String,
    iconResId: Int,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable(onClick = onClick)
            .shadow(
                elevation = 8.dp,
                ambientColor = NeonPurpleGlow,
                spotColor = NeonPurpleGlow,
                shape = RoundedCornerShape(16.dp)
            ),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1A1A2E)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            try {
                Image(
                    painter = painterResource(id = iconResId),
                    contentDescription = title,
                    modifier = Modifier.size(48.dp)
                )
            } catch (e: Exception) {
                // Fallback if icon resource doesn't exist
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(
                            NeonPurple.copy(alpha = 0.2f),
                            shape = RoundedCornerShape(8.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = title.first().toString(),
                        color = NeonPurple,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = description,
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }
    }
}
