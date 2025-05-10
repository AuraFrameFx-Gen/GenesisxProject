package dev.aurakai.auraframefx.ui.theme

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.Window
import androidx.core.view.WindowInsetsControllerCompat
import dev.aurakai.auraframefx.ai.EmotionState
import timber.log.Timber

/**
 * AuraThemeManager - Manages dynamic theming based on Aura's emotional state
 *
 * This class provides methods to subtly alter the app's appearance based on
 * Aura's current emotional state, creating an ambient awareness of her mood.
 */
object AuraThemeManager {

    // Theme color constants
    private val EXCITED_COLORS = ThemeColors(
        primary = Color.parseColor("#FF00FF"),    // Vibrant magenta
        secondary = Color.parseColor("#00FFFF"),  // Cyan
        accent = Color.parseColor("#FFFFFF"),     // White
        background = Color.parseColor("#1A0A1A")  // Dark magenta-tinted black
    )
    
    private val HAPPY_COLORS = ThemeColors(
        primary = Color.parseColor("#33FF33"),    // Bright green
        secondary = Color.parseColor("#00DDFF"),  // Sky blue
        accent = Color.parseColor("#FFFFFF"),     // White
        background = Color.parseColor("#0A1A0A")  // Dark green-tinted black
    )
    
    private val NEUTRAL_COLORS = ThemeColors(
        primary = Color.parseColor("#8000FF"),    // Purple
        secondary = Color.parseColor("#00AAFF"),  // Blue
        accent = Color.parseColor("#CCCCCC"),     // Light gray
        background = Color.parseColor("#0A0A14")  // Dark blue-tinted black
    )
    
    private val CONCERNED_COLORS = ThemeColors(
        primary = Color.parseColor("#FFA500"),    // Orange
        secondary = Color.parseColor("#FFDD00"),  // Yellow
        accent = Color.parseColor("#DDDDDD"),     // Gray
        background = Color.parseColor("#1A0F0A")  // Dark orange-tinted black
    )
    
    private val FRUSTRATED_COLORS = ThemeColors(
        primary = Color.parseColor("#FF0000"),    // Red
        secondary = Color.parseColor("#FF5500"),  // Orange-red
        accent = Color.parseColor("#BBBBBB"),     // Dark gray
        background = Color.parseColor("#1A0A0A")  // Dark red-tinted black
    )
    
    /**
     * Apply theme colors based on Aura's current emotional state
     *
     * @param activity The activity whose theme should be updated
     * @param emotion The current emotional state
     * @param intensity How strongly to apply the theme (0.0-1.0)
     */
    fun applyEmotionalTheme(activity: Activity, emotion: EmotionState, intensity: Float = 0.3f) {
        try {
            val window = activity.window
            val colors = getColorsForEmotion(emotion)
            
            // Apply status bar and navigation bar colors
            window.statusBarColor = colors.primary
            window.navigationBarColor = colors.background
            
            // Set appropriate status bar text color (light or dark)
            setLightStatusBar(window, isColorDark(colors.primary))
            
            // In a real implementation, we would also update MaterialTheme colors
            // or apply these colors to a theme overlay
            
            Timber.d("Applied emotional theme for ${emotion.name} with intensity $intensity")
            
        } catch (e: Exception) {
            Timber.e(e, "Error applying emotional theme")
        }
    }
    
    /**
     * Apply a subtle ambient glow effect to a view based on emotion
     *
     * @param view The view to apply the glow to
     * @param emotion The current emotional state
     * @param intensity How strongly to apply the effect (0.0-1.0)
     */
    fun applyEmotionalGlow(view: View, emotion: EmotionState, intensity: Float = 0.5f) {
        try {
            val colors = getColorsForEmotion(emotion)
            
            // Create a subtle glow effect using elevation and overlay
            view.elevation = 4f * intensity
            
            // In a real implementation, we would apply a proper glow drawable
            // or use a shadow layer with the appropriate color
            
            Timber.d("Applied emotional glow for ${emotion.name} with intensity $intensity")
            
        } catch (e: Exception) {
            Timber.e(e, "Error applying emotional glow")
        }
    }
    
    /**
     * Get theme colors for the specified emotion
     */
    private fun getColorsForEmotion(emotion: EmotionState): ThemeColors {
        return when (emotion) {
            EmotionState.Excited -> EXCITED_COLORS
            EmotionState.Happy -> HAPPY_COLORS
            EmotionState.Neutral -> NEUTRAL_COLORS
            EmotionState.Concerned -> CONCERNED_COLORS
            EmotionState.Frustrated -> FRUSTRATED_COLORS
        }
    }
    
    /**
     * Set the status bar to use light or dark text
     */
    private fun setLightStatusBar(window: Window, isLightStatusBar: Boolean) {
        WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = isLightStatusBar
    }
    
    /**
     * Determine if a color is "dark" and should have light text
     */
    private fun isColorDark(color: Int): Boolean {
        // Calculate luminance (standard formula)
        val r = Color.red(color) / 255.0
        val g = Color.green(color) / 255.0
        val b = Color.blue(color) / 255.0
        
        val luminance = 0.299 * r + 0.587 * g + 0.114 * b
        
        // Return true if color is light (should have dark text)
        return luminance > 0.5
    }
    
    /**
     * Data class to hold theme colors
     */
    data class ThemeColors(
        val primary: Int,
        val secondary: Int,
        val accent: Int,
        val background: Int
    )
}
