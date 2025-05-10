package dev.aurakai.auraframefx.ui.components

import android.content.Context
import android.graphics.PixelFormat
import android.graphics.Rect
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import com.airbnb.lottie.LottieAnimationView
import dev.aurakai.auraframefx.R
import dev.aurakai.auraframefx.ai.EmotionState
import dev.aurakai.auraframefx.utils.DisplayUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.max

/**
 * KaiNotchBar - An always-on-display AI assistant that lives in the notch area
 *
 * This component provides a persistent interface for Kai, a complementary AI to Aura,
 * that resides in the notch/status bar area of the device. Kai provides quick access
 * to AI features and ambient information.
 *
 * Created as part of the AuraFrameFX ecosystem
 */
class KaiNotchBar(context: Context, attrs: AttributeSet? = null) : FrameLayout(context, attrs) {

    // UI components
    private lateinit var kaiAnimationView: LottieAnimationView
    private lateinit var pulseAnimationView: LottieAnimationView
    
    // Animation states
    private var currentState = KaiState.IDLE
    private var currentEmotion = EmotionState.Neutral
    
    // Touch handling
    private var initialX = 0f
    private var initialY = 0f
    private var isDragging = false
    
    // Callback for user interactions
    var onInteractionListener: OnKaiInteractionListener? = null
    
    // Window manager for overlay display
    private var windowManager: WindowManager? = null
    private var layoutParams: WindowManager.LayoutParams? = null
    private var isAttachedToWindow = false
    
    init {
        // Initialize the view
        LayoutInflater.from(context).inflate(R.layout.view_kai_notch_bar, this, true)
        
        // Get animation views
        kaiAnimationView = findViewById(R.id.kai_animation)
        pulseAnimationView = findViewById(R.id.kai_pulse_animation)
        
        // Set up initial state
        updateState(KaiState.IDLE)
    }
    
    /**
     * Attach Kai to the window as an overlay
     */
    fun attachToWindow() {
        if (isAttachedToWindow) return
        
        try {
            val displayMetrics = DisplayUtils.getDisplayMetrics(context)
            val notchHeight = getNotchHeight()
            
            // Calculate optimal position based on notch
            val xPos = displayMetrics.widthPixels - dpToPx(60)
            val yPos = notchHeight + dpToPx(5)
            
            // Create window parameters
            layoutParams = WindowManager.LayoutParams().apply {
                type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                } else {
                    WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
                }
                flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                        WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
                format = PixelFormat.TRANSLUCENT
                width = dpToPx(50)
                height = dpToPx(50)
                gravity = Gravity.TOP or Gravity.START
                x = xPos
                y = yPos
            }
            
            // Get window manager and add view
            windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
            windowManager?.addView(this, layoutParams)
            isAttachedToWindow = true
            
            // Start idle animation
            pulseAnimationView.playAnimation()
            
            Timber.d("KaiNotchBar attached to window")
            
        } catch (e: Exception) {
            Timber.e(e, "Failed to attach KaiNotchBar to window")
        }
    }
    
    /**
     * Detach Kai from the window
     */
    fun detachFromWindow() {
        if (!isAttachedToWindow) return
        
        try {
            windowManager?.removeView(this)
            isAttachedToWindow = false
            Timber.d("KaiNotchBar detached from window")
            
        } catch (e: Exception) {
            Timber.e(e, "Failed to detach KaiNotchBar from window")
        }
    }
    
    /**
     * Update Kai's state with an optional animation
     */
    fun updateState(state: KaiState, animate: Boolean = true) {
        if (currentState == state) return
        currentState = state
        
        val animationFile = when (state) {
            KaiState.IDLE -> "kai_idle.json"
            KaiState.LISTENING -> "kai_listening.json"
            KaiState.SPEAKING -> "kai_speaking.json"
            KaiState.THINKING -> "kai_thinking.json"
            KaiState.ALERT -> "kai_alert.json"
        }
        
        if (animate) {
            kaiAnimationView.setAnimation(animationFile)
            kaiAnimationView.playAnimation()
        } else {
            kaiAnimationView.setAnimation(animationFile)
            kaiAnimationView.progress = 0.5f
        }
    }
    
    /**
     * Update Kai's emotional state
     */
    fun updateEmotion(emotion: EmotionState) {
        if (currentEmotion == emotion) return
        currentEmotion = emotion
        
        // Update colors based on emotion
        val primaryColor = when (emotion) {
            EmotionState.Excited -> "#00FFFF" // Cyan
            EmotionState.Happy -> "#33FF33" // Green
            EmotionState.Neutral -> "#FFFFFF" // White
            EmotionState.Concerned -> "#FFAA00" // Orange
            EmotionState.Frustrated -> "#FF0000" // Red
        }
        
        // Apply color to the animation
        // In a real implementation, we'd use dynamic property animation or tint
        pulseAnimationView.setColorFilter(android.graphics.Color.parseColor(primaryColor))
        
        // Show a quick pulse animation to indicate the change
        flashPulse()
    }
    
    /**
     * Flash the pulse animation to indicate an emotion change
     */
    private fun flashPulse() {
        pulseAnimationView.speed = 2.0f
        pulseAnimationView.playAnimation()
        
        // Reset to normal speed after the flash
        CoroutineScope(Dispatchers.Main).launch {
            delay(500)
            pulseAnimationView.speed = 1.0f
        }
    }
    
    /**
     * Simulate Kai speaking with mouth animation
     */
    fun speak(text: String, onComplete: () -> Unit) {
        updateState(KaiState.SPEAKING)
        
        // In a real implementation, we'd use text-to-speech and synchronize
        // animation with speech phonemes
        
        // For now, just animate for a duration based on text length
        val speakDuration = max(1500, text.length * 50).toLong()
        
        CoroutineScope(Dispatchers.Main).launch {
            delay(speakDuration)
            updateState(KaiState.IDLE)
            onComplete()
        }
    }
    
    /**
     * Get the height of the device notch
     */
    private fun getNotchHeight(): Int {
        // Attempt to detect notch height
        // This is a simplified approach; real implementation would use
        // DisplayCutout on Android P+ or device-specific methods
        
        val statusBarHeight = DisplayUtils.getStatusBarHeight(context)
        return statusBarHeight
    }
    
    /**
     * Convert dp to pixels
     */
    private fun dpToPx(dp: Int): Int {
        val density = resources.displayMetrics.density
        return (dp * density).toInt()
    }
    
    /**
     * Handle touch events for dragging and interaction
     */
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                initialX = event.rawX
                initialY = event.rawY
                return true
            }
            
            MotionEvent.ACTION_MOVE -> {
                val dx = event.rawX - initialX
                val dy = event.rawY - initialY
                
                // Start dragging if moved enough
                if (!isDragging && (abs(dx) > 10 || abs(dy) > 10)) {
                    isDragging = true
                }
                
                // Update position if dragging
                if (isDragging && layoutParams != null) {
                    layoutParams?.x = (layoutParams?.x ?: 0) + dx.toInt()
                    layoutParams?.y = (layoutParams?.y ?: 0) + dy.toInt()
                    windowManager?.updateViewLayout(this, layoutParams)
                    initialX = event.rawX
                    initialY = event.rawY
                }
                return true
            }
            
            MotionEvent.ACTION_UP -> {
                if (!isDragging) {
                    // This was a tap
                    onInteractionListener?.onKaiTapped()
                    
                    // Provide visual feedback
                    updateState(KaiState.ALERT)
                    CoroutineScope(Dispatchers.Main).launch {
                        delay(500)
                        updateState(KaiState.IDLE)
                    }
                }
                
                isDragging = false
                return true
            }
        }
        
        return super.onTouchEvent(event)
    }
    
    /**
     * Kai's possible states
     */
    enum class KaiState {
        IDLE,
        LISTENING,
        SPEAKING,
        THINKING,
        ALERT
    }
    
    /**
     * Interface for Kai interaction callbacks
     */
    interface OnKaiInteractionListener {
        fun onKaiTapped()
        fun onKaiLongPressed()
        fun onKaiSwipedLeft()
        fun onKaiSwipedRight()
    }
}
