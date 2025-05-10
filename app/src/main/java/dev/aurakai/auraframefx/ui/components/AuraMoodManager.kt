package dev.aurakai.auraframefx.ui.components

import android.content.Context
import android.graphics.PixelFormat
import android.view.Gravity
import android.view.WindowManager
import androidx.lifecycle.LifecycleOwner
import dev.aurakai.auraframefx.ai.EmotionState
import timber.log.Timber

/**
 * AuraMoodManager - Controls the display of Aura's mood orb as a floating element
 *
 * This class manages a floating AuraMoodOrb that can appear anywhere in the app
 * to provide ambient awareness of Aura's current emotional state.
 */
class AuraMoodManager private constructor(private val context: Context) {

    private var moodOrbView: AuraMoodOrb? = null
    private var windowManager: WindowManager? = null
    private var isShowing = false
    
    /**
     * Show the floating mood orb
     *
     * @param emotion The initial emotion to display
     * @param size The size of the orb in dp
     * @param x The x position (from screen edge)
     * @param y The y position (from screen edge)
     */
    fun showFloatingMoodOrb(
        emotion: EmotionState = EmotionState.Neutral,
        size: Int = DEFAULT_ORB_SIZE_DP,
        x: Int = DEFAULT_X_POSITION_DP,
        y: Int = DEFAULT_Y_POSITION_DP
    ) {
        if (isShowing) return
        
        try {
            val sizePx = dpToPx(size)
            
            // Create orb view
            moodOrbView = AuraMoodOrb(context).apply {
                setEmotion(emotion)
            }
            
            // Set up window params
            val params = WindowManager.LayoutParams(
                sizePx,
                sizePx,
                WindowManager.LayoutParams.TYPE_APPLICATION_PANEL,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                    WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
            )
            
            params.gravity = Gravity.TOP or Gravity.START
            params.x = dpToPx(x)
            params.y = dpToPx(y)
            
            // Add view to window
            windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager?.addView(moodOrbView, params)
            isShowing = true
            
            Timber.d("AuraMoodOrb is now showing")
            
        } catch (e: Exception) {
            Timber.e(e, "Error showing AuraMoodOrb")
        }
    }
    
    /**
     * Hide the floating mood orb
     */
    fun hideFloatingMoodOrb() {
        try {
            if (moodOrbView != null && isShowing) {
                windowManager?.removeView(moodOrbView)
                isShowing = false
                moodOrbView = null
            }
        } catch (e: Exception) {
            Timber.e(e, "Error hiding AuraMoodOrb")
        }
    }
    
    /**
     * Update the emotion of the floating orb
     *
     * @param emotion The new emotion to display
     */
    fun updateEmotion(emotion: EmotionState) {
        moodOrbView?.setEmotion(emotion)
    }
    
    /**
     * Utility function to convert dp to pixels
     */
    private fun dpToPx(dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }
    
    companion object {
        const val DEFAULT_ORB_SIZE_DP = 80
        const val DEFAULT_X_POSITION_DP = 20
        const val DEFAULT_Y_POSITION_DP = 100
        
        @Volatile private var INSTANCE: AuraMoodManager? = null
        
        fun getInstance(context: Context): AuraMoodManager {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: AuraMoodManager(context.applicationContext).also { INSTANCE = it }
            }
        }
    }
}
