package dev.aurakai.auraframefx.ui.neuralhexal

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.airbnb.lottie.LottieAnimationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dagger.hilt.android.AndroidEntryPoint
import dev.aurakai.auraframefx.R
import dev.aurakai.auraframefx.ai.ConversationState
import dev.aurakai.auraframefx.ai.EmotionState
import dev.aurakai.auraframefx.ui.animation.NeuralWhisperShowcase
import dev.aurakai.auraframefx.ui.components.AuraMoodOrb
import dev.aurakai.auraframefx.ui.theme.AuraThemeManager
import kotlinx.coroutines.launch
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Neural Whisper Fragment
 * 
 * Provides an interface for interacting with the Neural Whisper AI voice command system
 * Created by Claude-3 Opus (Anthropic) for AuraFrameFX
 */
@AndroidEntryPoint
class NeuralWhisperFragment : Fragment() {
    
    private val viewModel: NeuralWhisperViewModel by viewModels()    private lateinit var statusText: TextView
    private lateinit var conversationText: TextView
    private lateinit var emotionText: TextView
    private lateinit var animationView: LottieAnimationView
    private lateinit var activateButton: FloatingActionButton
    private lateinit var auraMoodOrb: AuraMoodOrb
    private lateinit var animationContainer: FrameLayout
    
    // Track if this is the first launch to show showcase
    private var isFirstLaunch = true
    
    // Request microphone permission
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startListening()
        } else {
            Toast.makeText(
                requireContext(),
                "Microphone permission is required for Neural Whisper",
                Toast.LENGTH_LONG
            ).show()
        }
    }
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_neural_whisper, container, false)
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
          // Initialize views
        statusText = view.findViewById(R.id.text_status)
        conversationText = view.findViewById(R.id.text_conversation)
        emotionText = view.findViewById(R.id.text_emotion)
        animationView = view.findViewById(R.id.animation_voice)
        auraMoodOrb = view.findViewById(R.id.aura_mood_orb)
        animationContainer = view.findViewById(R.id.animation_container)
        activateButton = view.findViewById(R.id.btn_activate)
        
        // Set up click listener for activation button
        activateButton.setOnClickListener {
            checkMicrophonePermission()
        }
          // Set up long press listener to toggle between wave and mood orb
        animationContainer.setOnLongClickListener {
            toggleAnimationMode()
            true
        }
        
        // Show showcase animation on first launch
        if (isFirstLaunch) {
            // Hide all views initially for animation
            view.alpha = 0f
            
            view.postDelayed({
                view.alpha = 1f
                showFirstLaunchShowcase()
                isFirstLaunch = false
            }, 500)
        }
        
        // Observe conversation state
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.conversationState.collect { state ->
                    updateUiForState(state)
                }
            }
        }
        
        // Observe emotion state
        viewModel.emotionState.observe(viewLifecycleOwner) { emotion ->
            updateEmotionUi(emotion)
        }
    }
    
    private fun checkMicrophonePermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.RECORD_AUDIO
            ) == PackageManager.PERMISSION_GRANTED -> {
                startListening()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.RECORD_AUDIO) -> {
                Toast.makeText(
                    requireContext(),
                    "Neural Whisper needs microphone access to process voice commands",
                    Toast.LENGTH_LONG
                ).show()
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
            else -> {
                requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
            }
        }
    }
    
    private fun startListening() {
        try {
            viewModel.startListening()
        } catch (e: Exception) {
            Timber.e(e, "Error starting voice listening")
            Toast.makeText(
                requireContext(),
                "Error: ${e.message}",
                Toast.LENGTH_SHORT
            ).show()
        }
    }
      private fun updateUiForState(state: ConversationState) {
        when (state) {
            is ConversationState.Idle -> {
                statusText.text = "Tap microphone to activate Neural Whisper"
                animationView.pauseAnimation()
                activateButton.isEnabled = true
                
                // Show mood orb in idle state
                if (!auraMoodOrb.isVisible && animationView.isVisible) {
                    toggleAnimationMode()
                }
            }
            
            is ConversationState.Listening -> {
                statusText.text = "Listening... speak now"
                
                // Show voice waves while listening
                if (!animationView.isVisible) {
                    toggleAnimationMode()
                }
                
                animationView.playAnimation()
                activateButton.isEnabled = false
            }
            
            is ConversationState.Processing -> {
                statusText.text = "Processing your command..."
                animationView.playAnimation()
                activateButton.isEnabled = false
            }
            
            is ConversationState.Ready -> {
                statusText.text = "Command processed"
                animationView.pauseAnimation()
                activateButton.isEnabled = true
                
                // Add the response to the conversation
                val timestamp = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
                val conversationEntry = "[$timestamp] You: ${viewModel.lastUserInput}\n" +
                        "[$timestamp] Neural Whisper: ${state.response}\n\n"
                
                conversationText.append(conversationEntry)
                
                // Show mood orb after processing
                if (!auraMoodOrb.isVisible) {
                    toggleAnimationMode()
                }
            }
            
            is ConversationState.Error -> {
                statusText.text = "Error: ${state.message}"
                animationView.pauseAnimation()
                activateButton.isEnabled = true
                
                Toast.makeText(
                    requireContext(),
                    "Error: ${state.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
      /**
     * Toggles between voice wave animation and mood orb display
     */
    private fun toggleAnimationMode() {
        if (animationView.isVisible) {
            animationView.visibility = View.GONE
            auraMoodOrb.visibility = View.VISIBLE
            auraMoodOrb.observeEmotionState(viewLifecycleOwner, viewModel.emotionState)
        } else {
            auraMoodOrb.visibility = View.GONE
            animationView.visibility = View.VISIBLE
        }
    }
    
    /**
     * Show the first launch showcase animation
     */
    private fun showFirstLaunchShowcase() {
        // Disable button during showcase
        activateButton.isEnabled = false
        
        // Run showcase animation
        view?.let { rootView ->
            NeuralWhisperShowcase.playShowcaseAnimation(rootView) {
                // After animation completes, enable the button
                activateButton.isEnabled = true
                
                // Show a welcome toast
                context?.let { ctx ->
                    Toast.makeText(
                        ctx,
                        "Welcome to Neural Whisper! Long-press the orb to toggle display mode",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
      private fun updateEmotionUi(emotion: EmotionState) {
        val emotionText = when (emotion) {
            EmotionState.Excited -> "EMOTION: EXCITED"
            EmotionState.Happy -> "EMOTION: HAPPY"
            EmotionState.Neutral -> "EMOTION: NEUTRAL"
            EmotionState.Concerned -> "EMOTION: CONCERNED"
            EmotionState.Frustrated -> "EMOTION: FRUSTRATED"
        }
        
        val emotionColor = when (emotion) {
            EmotionState.Excited -> "#FF00FF" // Magenta
            EmotionState.Happy -> "#33FF33" // Green
            EmotionState.Neutral -> "#FFFF00" // Yellow
            EmotionState.Concerned -> "#FFA500" // Orange
            EmotionState.Frustrated -> "#FF0000" // Red
        }
        
        this.emotionText.text = emotionText
        this.emotionText.setTextColor(android.graphics.Color.parseColor(emotionColor))
        
        // Update the theme based on emotion
        activity?.let { 
            AuraThemeManager.applyEmotionalTheme(it, emotion)
            
            // Apply glow effect to conversation card
            view?.findViewById<View>(R.id.card_conversation)?.let { card ->
                AuraThemeManager.applyEmotionalGlow(card, emotion)
            }
        }
        
        // Update the mood orb with current emotion (if visible)
        if (auraMoodOrb.isVisible) {
            auraMoodOrb.setEmotion(emotion)
        }
    }
}
