package dev.aurakai.auraframefx.ui.components

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import dev.aurakai.auraframefx.R
import dev.aurakai.auraframefx.ai.EmotionState
import timber.log.Timber

/**
 * AuraMoodOrb - A specialized component for displaying Aura's emotional state
 *
 * This component renders a Lottie animation that changes color and animation speed
 * based on the current emotional state of Aura, providing visual feedback to the user
 * about how Aura is "feeling" during their interaction.
 *
 * Created as part of the Neural Whisper feature for AuraFrameFX
 */
class AuraMoodOrb @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val animationView: LottieAnimationView
    private var currentEmotion = EmotionState.Neutral

    init {
        LayoutInflater.from(context).inflate(R.layout.view_aura_mood_orb, this, true)
        animationView = findViewById(R.id.animation_mood_orb)
        
        // Set default animation
        setEmotion(EmotionState.Neutral)
    }

    /**
     * Set the emotional state of Aura
     *
     * @param emotion The current emotion state to display
     */
    fun setEmotion(emotion: EmotionState) {
        if (emotion == currentEmotion) return
        
        currentEmotion = emotion
        
        Timber.d("Setting AuraMoodOrb emotion to: $emotion")
        
        // Adjust animation properties based on emotion
        when (emotion) {
            EmotionState.Excited -> {
                // Fast pulsing magenta
                animationView.speed = 1.5f
                setOrbColor("#FF00FF", "#00FFFF")
            }
            EmotionState.Happy -> {
                // Medium pulsing green-cyan
                animationView.speed = 1.2f
                setOrbColor("#33FF33", "#00FFFF")
            }
            EmotionState.Neutral -> {
                // Normal pulsing blue-purple
                animationView.speed = 1.0f
                setOrbColor("#8000FF", "#00FFFF")
            }
            EmotionState.Concerned -> {
                // Slow pulsing orange
                animationView.speed = 0.8f
                setOrbColor("#FFA500", "#00FFFF")
            }
            EmotionState.Frustrated -> {
                // Very slow pulsing red
                animationView.speed = 0.6f
                setOrbColor("#FF0000", "#00FFFF")
            }
        }
        
        // Ensure the animation is playing
        if (!animationView.isAnimating) {
            animationView.playAnimation()
        }
    }
    
    /**
     * Observe an emotion state LiveData and update automatically
     *
     * @param lifecycleOwner The LifecycleOwner to respect for observation
     * @param emotionState The emotion state LiveData to observe
     */
    fun observeEmotionState(lifecycleOwner: LifecycleOwner, emotionState: LiveData<EmotionState>) {
        emotionState.observe(lifecycleOwner) { emotion ->
            setEmotion(emotion)
        }
    }
    
    /**
     * Set the core and outer colors of the orb
     *
     * @param coreColor The color for the central orb (in hex format)
     * @param outerColor The color for the outer ring (in hex format)
     */
    private fun setOrbColor(coreColor: String, outerColor: String) {
        // Convert hex colors to int colors
        val coreColorInt = android.graphics.Color.parseColor(coreColor)
        val outerColorInt = android.graphics.Color.parseColor(outerColor)
        
        // Set the core color - targeting the fill of Core Orb layer
        animationView.addValueCallback(
            KeyPath("Core Orb", "Ellipse 1", "Fill 1"),
            LottieProperty.COLOR,
            { coreColorInt }
        )
        
        // Set the outer ring color - targeting the stroke of Outer Aura layer
        animationView.addValueCallback(
            KeyPath("Outer Aura", "Ellipse 1", "Stroke 1"),
            LottieProperty.COLOR,
            { outerColorInt }
        )
    }
}
