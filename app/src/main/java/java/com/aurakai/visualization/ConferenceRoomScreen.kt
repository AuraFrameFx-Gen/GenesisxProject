package com.aurakai.visualization

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Message data class
private data class ChatMessage(val sender: String, val text: String, val color: Color)

@Composable
fun ConferenceRoomScreen(
    userName: String = "Matt",
    agents: List<Pair<String, Color>> = listOf(
        "Aura" to Color(0xFF80D8FF),
        "Kai" to Color(0xFFFFAB91),
        "Cascade" to Color(0xFFB388FF)
    ),
) {
    var chatLog by remember {
        mutableStateOf(
            listOf(
                ChatMessage("Aura", "Welcome to the Conference Room!", Color(0xFF80D8FF)),
                ChatMessage("Kai", "Let's build something amazing.", Color(0xFFFFAB91)),
                ChatMessage("Cascade", "I'm here to help coordinate!", Color(0xFFB388FF))
            )
        )
    }
    var input by remember { mutableStateOf("") }
    rememberCoroutineScope()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Conference Room", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(8.dp))
        HorizontalDivider()
        Spacer(Modifier.height(8.dp))
        // Chat log
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth(),
            reverseLayout = false
        ) {
            items(chatLog) { message ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    AgentAvatar(sender = message.sender, color = message.color)
                    Spacer(Modifier.width(8.dp))
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = message.color.copy(alpha = 0.12f),
                        modifier = Modifier.animateContentSize(
                            animationSpec = spring(
                                dampingRatio = Spring.DampingRatioMediumBouncy,
                                stiffness = Spring.StiffnessLow
                            )
                        )
                    ) {
                        Text(
                            message.text,
                            modifier = Modifier.padding(12.dp),
                            fontSize = 16.sp
                        )
                    }
                }
            }
        }
        Spacer(Modifier.height(8.dp))
        // Input area
        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = input,
                onValueChange = { input = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Type a message...") },
                keyboardActions = KeyboardActions(onDone = {
                    if (input.isNotBlank()) {
                        chatLog = chatLog + ChatMessage(userName, input, Color(0xFF4CAF50))
                        input = ""
                    }
                })
            )
            Spacer(Modifier.width(8.dp))
            Button(onClick = {
                if (input.isNotBlank()) {
                    chatLog = chatLog + ChatMessage(userName, input, Color(0xFF4CAF50))
                    input = ""
                }
            }) {
                Text("Send")
            }
        }
        Spacer(Modifier.height(8.dp))
        // Quick reply buttons
        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Okay", "Got it", "Thanks!").forEach { quickReply ->
                Button(onClick = {
                    chatLog = chatLog + ChatMessage(userName, quickReply, Color(0xFF4CAF50))
                }) {
                    Text(quickReply)
                }
            }
        }
    }
}

@Composable
private fun AgentAvatar(sender: String, color: Color) {
    val avatarScale = remember { Animatable(1f) }
    LaunchedEffect(sender) {
        avatarScale.animateTo(1.2f, animationSpec = tween(150))
        avatarScale.animateTo(1f, animationSpec = tween(150))
    }
    Box(
        Modifier
            .size(40.dp)
            .clip(CircleShape)
            .background(color)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            sender.take(1),
            color = Color.White,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
        )
    }
}
