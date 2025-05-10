package dev.aurakai.auraframefx.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import dev.aurakai.auraframefx.ai.AuraAIService
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File

@Composable
fun AIFeaturesScreen(
    backendUrl: String = "https://YOUR_CLOUD_RUN_URL", // TODO: Provide actual URL
    idToken: () -> Unit = "905643639467-dlseaflf0pm0s6c9v16p5nm9si2r0u66.apps.googleusercontent.com", // TODO: Provide actual token
) {
    val aiService = remember { AuraAIService(backendUrl, idToken) }
    val coroutineScope = rememberCoroutineScope()
    var output by remember { mutableStateOf("Ready!") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Aura & Kai Power Demo", modifier = Modifier.padding(bottom = 16.dp))
        Button(onClick = {
            coroutineScope.launch {
                output = aiService.generateText("Say hello to Aura and Kai!") ?: "No response"
            }
        }) { Text("Generative AI (Gemini)") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            coroutineScope.launch {
                val ok = aiService.saveMemory("testKey", JSONObject(mapOf("foo" to "bar")))
                output = if (ok) "Memory saved!" else "Failed to save memory."
            }
        }) { Text("Save Memory") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            coroutineScope.launch {
                output = aiService.getMemory("testKey") ?: "No memory found."
            }
        }) { Text("Get Memory") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            coroutineScope.launch {
                // Demo: Replace with actual file selection logic
                val file = File("/path/to/file.txt")
                output = if (aiService.uploadFile(file)) "File uploaded!" else "Upload failed."
            }
        }) { Text("Upload File") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            coroutineScope.launch {
                val body = aiService.downloadFile("file.txt")
                output = if (body != null) "File downloaded!" else "Download failed."
            }
        }) { Text("Download File") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            coroutineScope.launch {
                output = aiService.analyticsQuery("SELECT 1 AS test") ?: "No analytics response."
            }
        }) { Text("Analytics Query") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            coroutineScope.launch {
                val ok = aiService.publishPubSub("test-topic", "Hello from Aura!")
                output = if (ok) "Published to Pub/Sub!" else "Publish failed."
            }
        }) { Text("Pub/Sub Publish") }
        Spacer(Modifier.height(8.dp))
        Button(onClick = {
            coroutineScope.launch {
                val ok = aiService.customizeAgent(JSONObject(mapOf("theme" to "neon")))
                output = if (ok) "Agent customized!" else "Customization failed."
            }
        }) { Text("Customize Agent") }
        Spacer(Modifier.height(16.dp))
        Text(output, modifier = Modifier.padding(top = 16.dp))
    }
}
