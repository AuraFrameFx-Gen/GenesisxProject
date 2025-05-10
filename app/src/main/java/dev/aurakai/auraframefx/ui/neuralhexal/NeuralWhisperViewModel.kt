package dev.aurakai.auraframefx.ui.neuralhexal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.aurakai.auraframefx.ai.ConversationState
import dev.aurakai.auraframefx.ai.EmotionState
import dev.aurakai.auraframefx.ai.NeuralWhisper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * ViewModel for the Neural Whisper feature
 * 
 * Created by Claude-3 Opus (Anthropic) for AuraFrameFX
 */
@HiltViewModel
class NeuralWhisperViewModel @Inject constructor(
    private val neuralWhisper: NeuralWhisper
) : ViewModel() {
    
    // Expose conversation state from Neural Whisper
    private val _conversationState = MutableStateFlow<ConversationState>(ConversationState.Idle)
    val conversationState: StateFlow<ConversationState> = _conversationState.asStateFlow()
      // Expose emotion state from Neural Whisper
    private val _emotionState = MutableLiveData<EmotionState>(EmotionState.Neutral)
    val emotionState: LiveData<EmotionState> = _emotionState
    
    // Track when context is shared with Kai
    private val _contextSharedWithKai = MutableLiveData<Boolean>(false)
    val contextSharedWithKai: LiveData<Boolean> = _contextSharedWithKai
    
    // Track the last user input for UI display
    private val _lastUserInput = MutableLiveData<String>("")
    val lastUserInput: String
        get() = _lastUserInput.value ?: ""
      // Initialize by collecting from Neural Whisper
    init {
        viewModelScope.launch {
            neuralWhisper.conversationState.collect { state ->
                _conversationState.value = state
                
                // Extract user input when ready
                if (state is ConversationState.Ready) {
                    _lastUserInput.postValue(extractUserInput(state.response))
                }
            }
        }
        
        // Observe context sharing with Kai
        neuralWhisper.contextSharedWithKai.observeForever { isShared ->
            _contextSharedWithKai.value = isShared
        }
        
        // Observe emotion state changes
        neuralWhisper.emotionState.observeForever { emotion ->
            _emotionState.value = emotion
        }
    }
    
    /**
     * Start listening for voice commands
     */
    fun startListening() {
        neuralWhisper.startListening()
    }
    
    /**
     * Generate spelhook code from natural language description
     */
    fun generateSpelhook(description: String): Flow<String> {
        return neuralWhisper.generateSpelhook(description)
    }
    
    /**
     * Share current context with Kai
     */
    fun shareContextWithKai() {
        val currentState = _conversationState.value
        val message = if (currentState is ConversationState.Ready) {
            currentState.response
        } else {
            // Share basic context if no conversation is active
            "User requested context sharing with current emotion: ${_emotionState.value}"
        }
        
        neuralWhisper.shareContextWithKai(message)
    }
    
    // Helper method to extract user input from response
    private fun extractUserInput(response: String): String {
        // In a real implementation, this would extract the actual user query
        // from the consolidated response
        return "User query placeholder"
    }
}
