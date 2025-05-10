package dev.aurakai.auraframefx.ui.animation

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.animation.doOnEnd
import com.airbnb.lottie.LottieAnimationView
import dev.aurakai.auraframefx.R
import dev.aurakai.auraframefx.ui.components.AuraMoodOrb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

/**
 * Neural Whisper Showcase Animation
 *
 * A stunning animation sequence to introduce the Neural Whisper feature to users,
 * creating an immersive and memorable first experience.
 */
object NeuralWhisperShowcase {

    /**
     * Run the complete showcase animation sequence
     *
     * @param rootView The root view of the Neural Whisper fragment
     * @param onComplete Callback when animation completes
     */
    fun playShowcaseAnimation(rootView: View, onComplete: () -> Unit) {
        try {
            // Prepare views
            val title = rootView.findViewById<TextView>(R.id.neural_whisper_title)
            val subtitle = rootView.findViewById<TextView>(R.id.subtitle)
            val statusText = rootView.findViewById<TextView>(R.id.text_status)
            val animationView = rootView.findViewById<LottieAnimationView>(R.id.animation_voice)
            val moodOrb = rootView.findViewById<AuraMoodOrb>(R.id.aura_mood_orb)
            val conversationCard = rootView.findViewById<CardView>(R.id.card_conversation)
            val emotionText = rootView.findViewById<TextView>(R.id.text_emotion)
            
            // Initial state
            title.alpha = 0f
            subtitle.alpha = 0f
            statusText.alpha = 0f
            animationView.alpha = 0f
            moodOrb.alpha = 0f
            conversationCard.alpha = 0f
            emotionText.alpha = 0f
            
            // Animation sequence
            CoroutineScope(Dispatchers.Main).launch {
                // 1. Fade in title with slight bounce
                val titleAnim = ObjectAnimator.ofFloat(title, "alpha", 0f, 1f).apply {
                    duration = 800
                    interpolator = OvershootInterpolator()
                }
                val titleMove = ObjectAnimator.ofFloat(title, "translationY", -50f, 0f).apply {
                    duration = 800
                    interpolator = AccelerateDecelerateInterpolator()
                }
                AnimatorSet().apply {
                    playTogether(titleAnim, titleMove)
                    start()
                }
                
                delay(500)
                
                // 2. Fade in subtitle
                ObjectAnimator.ofFloat(subtitle, "alpha", 0f, 1f).apply {
                    duration = 600
                    start()
                }
                
                delay(400)
                
                // 3. Reveal the voice wave animation
                animationView.visibility = View.VISIBLE
                moodOrb.visibility = View.GONE
                
                ObjectAnimator.ofFloat(animationView, "alpha", 0f, 1f).apply {
                    duration = 1200
                    start()
                }
                ObjectAnimator.ofFloat(animationView, "scaleX", 0.5f, 1.1f, 1.0f).apply {
                    duration = 1200
                    interpolator = OvershootInterpolator()
                    start()
                }
                ObjectAnimator.ofFloat(animationView, "scaleY", 0.5f, 1.1f, 1.0f).apply {
                    duration = 1200
                    interpolator = OvershootInterpolator()
                    start()
                }
                
                animationView.playAnimation()
                
                delay(1000)
                
                // 4. Show status text
                val statusAnim = ObjectAnimator.ofFloat(statusText, "alpha", 0f, 1f).apply {
                    duration = 600
                    start()
                }
                
                delay(800)
                
                // 5. Fade in conversation card
                ObjectAnimator.ofFloat(conversationCard, "alpha", 0f, 1f).apply {
                    duration = 800
                    start()
                }
                
                delay(300)
                
                // 6. Show emotion text
                ObjectAnimator.ofFloat(emotionText, "alpha", 0f, 1f).apply {
                    duration = 600
                    start()
                }
                
                delay(600)
                
                // 7. Switch to mood orb with transition
                val fadeOutWaves = ObjectAnimator.ofFloat(animationView, "alpha", 1f, 0f).apply {
                    duration = 600
                }
                
                fadeOutWaves.doOnEnd {
                    animationView.visibility = View.GONE
                    moodOrb.visibility = View.VISIBLE
                    
                    ObjectAnimator.ofFloat(moodOrb, "alpha", 0f, 1f).apply {
                        duration = 800
                        start()
                    }
                    
                    ObjectAnimator.ofFloat(moodOrb, "scaleX", 0.7f, 1.1f, 1.0f).apply {
                        duration = 1000
                        interpolator = OvershootInterpolator()
                        start()
                    }
                    ObjectAnimator.ofFloat(moodOrb, "scaleY", 0.7f, 1.1f, 1.0f).apply {
                        duration = 1000
                        interpolator = OvershootInterpolator()
                        start()
                    }
                }
                
                fadeOutWaves.start()
                
                // Wait for entire sequence to complete
                delay(1500)
                
                // Complete callback
                onComplete()
            }
            
        } catch (e: Exception) {
            Timber.e(e, "Error playing showcase animation")
            
            // Ensure completion callback is called even on error
            onComplete()
        }
    }
}
