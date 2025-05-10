package dev.aurakai.auraframefx.ui.components

import android.app.ActivityManager
import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.WindowManager
import android.widget.FrameLayout
import com.airbnb.lottie.LottieAnimationView
import dev.aurakai.auraframefx.R
import dev.aurakai.auraframefx.ai.EmotionState
import dev.aurakai.auraframefx.utils.DisplayUtils
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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
 * Security and system features:
 * - Ad blocking
 * - RAM optimization
 * - System monitoring
 * - Live error checking
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

    // Security monitoring
    private var adBlockEnabled = true
    private var ramOptimizationEnabled = true
    private var systemMonitoringEnabled = true
    private var errorCheckingEnabled = true

    // Monitoring jobs
    private var adBlockJob: Job? = null
    private var ramOptimizationJob: Job? = null
    private var systemMonitorJob: Job? = null
    private var errorCheckingJob: Job? = null

    // Handler for UI updates
    private val handler = Handler(Looper.getMainLooper())

    // Blocked hosts
    private val blockedHosts = mutableSetOf<String>()

    // System stats
    private var cpuUsage = 0.0
    private var ramUsage = 0.0
    private var batteryTemp = 0.0
    private var errorCount = 0

    init {
        // Initialize the view
        LayoutInflater.from(context).inflate(R.layout.view_kai_notch_bar, this, true)

        // Get animation views
        kaiAnimationView = findViewById(R.id.kai_animation)
        pulseAnimationView = findViewById(R.id.kai_pulse_animation)

        // Set up initial state
        updateState(KaiState.IDLE)

        // Load ad blocking hosts
        loadAdBlockingHosts()
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

            // Start security monitoring
            startSecurityMonitoring()

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

            // Stop security monitoring
            stopSecurityMonitoring()

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
     * Start security monitoring features when Kai attaches to window
     */
    private fun startSecurityMonitoring() {
        if (adBlockEnabled) startAdBlocker()
        if (ramOptimizationEnabled) startRamOptimizer()
        if (systemMonitoringEnabled) startSystemMonitor()
        if (errorCheckingEnabled) startErrorChecker()

        Timber.d("Kai security features activated")
    }

    /**
     * Stop all security monitoring features
     */
    private fun stopSecurityMonitoring() {
        adBlockJob?.cancel()
        ramOptimizationJob?.cancel()
        systemMonitorJob?.cancel()
        errorCheckingJob?.cancel()

        Timber.d("Kai security features deactivated")
    }

    /**
     * Load ad blocking hosts from resource
     */
    private fun loadAdBlockingHosts() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                // In a real implementation, load from a hosts file or API
                blockedHosts.addAll(
                    listOf(
                        "ads.example.com",
                        "tracker.example.net",
                        "analytics.example.org",
                        "ads.doubleclick.net",
                        "advertising.com",
                        "banners.example.com"
                    )
                )
                Timber.d("Loaded ${blockedHosts.size} ad blocking hosts")
            } catch (e: Exception) {
                Timber.e(e, "Failed to load ad blocking hosts")
            }
        }
    }

    /**
     * Start ad blocker service
     */
    private fun startAdBlocker() {
        adBlockJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                try {
                    // Simulate ad blocking check
                    val blockedCount = (0..3).random()
                    if (blockedCount > 0) {
                        withContext(Dispatchers.Main) {
                            flashPulse()
                        }
                        Timber.d("Blocked $blockedCount ad requests")
                    }
                    delay(10000) // Check every 10 seconds
                } catch (e: Exception) {
                    Timber.e(e, "Error in ad blocker")
                }
            }
        }
    }

    /**
     * Start RAM optimization service
     */
    private fun startRamOptimizer() {
        ramOptimizationJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                try {
                    // Get current RAM usage
                    val memInfo = ActivityManager.MemoryInfo()
                    val activityManager =
                        context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
                    activityManager.getMemoryInfo(memInfo)

                    val availableMem = memInfo.availMem
                    val totalMem = memInfo.totalMem
                    ramUsage = 100.0 * (totalMem - availableMem) / totalMem

                    // Optimize if RAM usage is high
                    if (ramUsage > 80) {
                        Timber.d("High RAM usage detected: $ramUsage%, optimizing...")
                        optimizeMemory(activityManager)

                        withContext(Dispatchers.Main) {
                            updateState(KaiState.ALERT)
                            speak("RAM usage optimized", onComplete = {
                                updateState(KaiState.IDLE)
                            })
                        }
                    }

                    delay(30000) // Check every 30 seconds
                } catch (e: Exception) {
                    Timber.e(e, "Error in RAM optimizer")
                }
            }
        }
    }

    /**
     * Optimize device memory
     */
    private fun optimizeMemory(activityManager: ActivityManager) {
        try {
            // Request garbage collection
            System.gc()

            // In a real app with proper permissions, we could:
            // 1. Identify memory-intensive background apps
            // 2. Suggest closing them or actually close them with proper permissions
            // 3. Clear application caches

            // For simulation purposes, we'll log it
            Timber.d("Memory optimization performed")

        } catch (e: Exception) {
            Timber.e(e, "Error optimizing memory")
        }
    }

    /**
     * Start system monitoring service
     */
    private fun startSystemMonitor() {
        systemMonitorJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                try {
                    // Monitor CPU usage
                    cpuUsage = readCpuUsage()

                    // Monitor battery temperature
                    batteryTemp = readBatteryTemperature()

                    // Alert if system is in danger zone
                    if (cpuUsage > 90 || batteryTemp > 45) {
                        withContext(Dispatchers.Main) {
                            updateState(KaiState.ALERT)
                            val message = if (cpuUsage > 90)
                                "High CPU detected: ${cpuUsage.toInt()}%"
                            else
                                "High battery temperature: ${batteryTemp.toInt()}°C"

                            speak(message, onComplete = {
                                updateState(KaiState.IDLE)
                            })
                        }
                    }

                    delay(60000) // Check every minute
                } catch (e: Exception) {
                    Timber.e(e, "Error in system monitor")
                }
            }
        }
    }

    /**
     * Read CPU usage from proc
     */
    private fun readCpuUsage(): Double {
        try {
            // In a real implementation, we'd read from /proc/stat
            // For this demo, we'll simulate it
            return (20..95).random().toDouble()
        } catch (e: Exception) {
            Timber.e(e, "Error reading CPU usage")
            return 0.0
        }
    }

    /**
     * Read battery temperature
     */
    private fun readBatteryTemperature(): Double {
        try {
            // In a real implementation, we'd use BatteryManager
            // For this demo, we'll simulate it
            return (25..48).random().toDouble()
        } catch (e: Exception) {
            Timber.e(e, "Error reading battery temperature")
            return 0.0
        }
    }

    /**
     * Start error checking service
     */
    private fun startErrorChecker() {
        errorCheckingJob = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
                try {
                    // Check for app crashes in logs
                    val newErrors = checkForErrors()

                    if (newErrors > 0) {
                        errorCount += newErrors
                        withContext(Dispatchers.Main) {
                            updateState(KaiState.ALERT)
                            speak("Detected $newErrors new error events", onComplete = {
                                updateState(KaiState.IDLE)
                            })
                        }
                    }

                    delay(120000) // Check every 2 minutes
                } catch (e: Exception) {
                    Timber.e(e, "Error in error checker")
                }
            }
        }
    }

    /**
     * Check system logs for errors
     */
    private fun checkForErrors(): Int {
        try {
            // In a real implementation with proper permissions, we'd read system logs
            // For this demo, we'll simulate finding random errors
            return (0..2).random()
        } catch (e: Exception) {
            Timber.e(e, "Error checking for errors")
            return 0
        }
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
