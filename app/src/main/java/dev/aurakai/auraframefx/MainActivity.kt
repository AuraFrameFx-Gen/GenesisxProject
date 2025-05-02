package dev.aurakai.auraframefx

import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import dev.aurakai.auraframefx.data.SecurePreferences
import dev.aurakai.auraframefx.ui.IntroScreen
import dev.aurakai.auraframefx.ui.StaticOrb
import dev.aurakai.auraframefx.ui.SwipeMenuScreen
import dev.aurakai.auraframefx.ui.theme.AuraFrameFXTheme

class MainActivity : ComponentActivity() {
    private lateinit var securePrefs: SecurePreferences
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        try {
            super.onCreate(savedInstanceState)

            // Initialize preferences
            prefs = getSharedPreferences("xposed_status_prefs", MODE_PRIVATE)
            if (!prefs.getBoolean("module_active", false)) {
                throw RuntimeException("Module not activated")
            }

            // Initialize secure preferences
            securePrefs = SecurePreferences(this)

            setContent {
                AuraFrameFXTheme {
                    Box(modifier = Modifier.fillMaxSize()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            IntroScreen {
                                Box(modifier = Modifier.fillMaxSize()) {
                                    SwipeMenuScreen()
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(24.dp),
                                        contentAlignment = Alignment.BottomEnd
                                    ) {
                                        StaticOrb()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Throwable) {
            e.printStackTrace()
            throw RuntimeException("Failed to initialize MainActivity", e)
        }
    }

    override fun onDestroy() {
        securePrefs.clearAllTokens()
        super.onDestroy()
    }
}