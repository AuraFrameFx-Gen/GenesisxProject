package dev.aurakai.auraframefx.ui.security

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import dev.aurakai.auraframefx.ui.theme.*

@Composable
fun KaiToolboxScreen(
    viewModel: KaiToolboxViewModel = viewModel(),
    onBack: () -> Unit
) {
    val scrollState = rememberScrollState()
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF000000),
                        Color(0xFF0A0A1A)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Header
            NeonText(
                text = "Kai Security Toolbox",
                fontSize = 28.sp,
                color = NeonTeal,
                glowColor = NeonTealGlow,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            
            Text(
                "Neural Whisper Enhanced Security",
                fontSize = 16.sp,
                color = Color.White,
                modifier = Modifier.padding(bottom = 30.dp)
            )
            
            // Security Feature Cards
            SecurityFeatureCard(
                title = "Ad Blocking",
                description = "Block unwanted ads and trackers",
                isEnabled = viewModel.adBlockingEnabled.value,
                onToggleChange = { viewModel.updateAdBlocking(it) }
            )
            
            SecurityFeatureCard(
                title = "RAM Optimization",
                description = "Optimize memory usage automatically",
                isEnabled = viewModel.ramOptimizationEnabled.value,
                onToggleChange = { viewModel.updateRamOptimization(it) }
            )
            
            SecurityFeatureCard(
                title = "System Monitoring",
                description = "Monitor CPU usage and battery temperature",
                isEnabled = viewModel.systemMonitoringEnabled.value,
                onToggleChange = { viewModel.updateSystemMonitoring(it) }
            )
            
            SecurityFeatureCard(
                title = "Error Checking",
                description = "Detect and report system errors in real-time",
                isEnabled = viewModel.errorCheckingEnabled.value,
                onToggleChange = { viewModel.updateErrorChecking(it) }
            )
            
            Spacer(modifier = Modifier.height(20.dp))
            
            // Notch Bar Position Control
            NotchBarPositionControl(
                position = viewModel.notchPosition.value,
                onPositionChange = { viewModel.updateNotchPosition(it) }
            )
            
            Spacer(modifier = Modifier.height(30.dp))
            
            // Ad Blocking Host List Management
            if (viewModel.adBlockingEnabled.value) {
                AdBlockingHostList(
                    hostList = viewModel.adBlockingHosts.value,
                    onAddHost = { viewModel.addHostToBlockList(it) },
                    onRemoveHost = { viewModel.removeHostFromBlockList(it) }
                )
            }
            
            Spacer(modifier = Modifier.height(40.dp))
            
            // Back Button
            Button(
                onClick = onBack,
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonPurple
                ),
                modifier = Modifier
                    .shadow(
                        elevation = 6.dp,
                        ambientColor = NeonPurpleGlow,
                        spotColor = NeonPurpleGlow
                    )
            ) {
                Text("Back to AuraShield", color = Color.White)
            }
        }
    }
}

@Composable
fun SecurityFeatureCard(
    title: String,
    description: String,
    isEnabled: Boolean,
    onToggleChange: (Boolean) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        NeonTeal.copy(alpha = 0.5f),
                        NeonPurple.copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .shadow(
                elevation = if (isEnabled) 6.dp else 1.dp,
                ambientColor = if (isEnabled) NeonTealGlow else Color.Transparent,
                spotColor = if (isEnabled) NeonTealGlow else Color.Transparent,
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF121220)
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = if (isEnabled) NeonTeal else Color.White,
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
            
            Switch(
                checked = isEnabled,
                onCheckedChange = onToggleChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = NeonTeal,
                    checkedTrackColor = NeonTealGlow.copy(alpha = 0.5f),
                    uncheckedThumbColor = Color.DarkGray,
                    uncheckedTrackColor = Color.DarkGray.copy(alpha = 0.3f)
                )
            )
        }
    }
}

@Composable
fun NotchBarPositionControl(
    position: Float,
    onPositionChange: (Float) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        NeonPink.copy(alpha = 0.5f),
                        NeonPurple.copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF121220)
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Kai Notch Bar Position",
                color = NeonPink,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Adjust the position of Kai's notch bar",
                color = Color.Gray,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Slider(
                value = position,
                onValueChange = onPositionChange,
                valueRange = 0f..1f,
                colors = SliderDefaults.colors(
                    thumbColor = NeonPink,
                    activeTrackColor = NeonPinkGlow.copy(alpha = 0.7f),
                    inactiveTrackColor = Color.DarkGray
                ),
                modifier = Modifier.fillMaxWidth()
            )
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text("Top", color = Color.Gray, fontSize = 12.sp)
                Text("Bottom", color = Color.Gray, fontSize = 12.sp)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdBlockingHostList(
    hostList: List<String>,
    onAddHost: (String) -> Unit,
    onRemoveHost: (String) -> Unit
) {
    var newHost by remember { mutableStateOf("") }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .border(
                width = 1.dp,
                brush = Brush.horizontalGradient(
                    colors = listOf(
                        NeonPurple.copy(alpha = 0.5f),
                        NeonPink.copy(alpha = 0.5f)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF121220)
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "Ad Blocking Host List",
                color = NeonPurple,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Add domains to block or remove existing ones",
                color = Color.Gray,
                fontSize = 14.sp
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Add new host field
            OutlinedTextField(
                value = newHost,
                onValueChange = { newHost = it },
                label = { Text("Enter domain to block", color = Color.Gray) },
                singleLine = true,
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = NeonPurple,
                    unfocusedBorderColor = Color.Gray,
                    textColor = Color.White,
                    cursorColor = NeonPurple
                ),
                modifier = Modifier.fillMaxWidth()
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Button(
                onClick = {
                    if (newHost.isNotEmpty()) {
                        onAddHost(newHost)
                        newHost = ""
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = NeonPurple
                ),
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Add Host")
            }
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // List of existing hosts
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF0A0A15))
                    .padding(8.dp)
            ) {
                if (hostList.isEmpty()) {
                    Text(
                        text = "No hosts added yet",
                        color = Color.Gray,
                        modifier = Modifier.padding(8.dp)
                    )
                } else {
                    hostList.forEach { host ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = host,
                                color = Color.White,
                                modifier = Modifier.weight(1f)
                            )
                            
                            Button(
                                onClick = { onRemoveHost(host) },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = NeonPink
                                ),
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .height(30.dp)
                            ) {
                                Text("Remove", fontSize = 12.sp)
                            }
                        }
                        
                        if (host != hostList.last()) {
                            Divider(
                                color = Color.DarkGray.copy(alpha = 0.5f),
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
