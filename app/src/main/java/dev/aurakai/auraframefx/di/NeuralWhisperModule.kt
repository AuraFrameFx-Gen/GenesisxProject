package dev.aurakai.auraframefx.di

import android.content.Context
import com.google.ai.client.generativeai.GenerativeModel
import com.google.firebase.vertexai.VertexAI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import dev.aurakai.auraframefx.ai.NeuralWhisper
import javax.inject.Singleton

/**
 * Dependency injection module for Neural Whisper
 * 
 * Created by Claude-3 Opus (Anthropic) for AuraFrameFX
 */
@Module
@InstallIn(SingletonComponent::class)
object NeuralWhisperModule {
    
    @Provides
    @Singleton
    fun provideNeuralWhisper(
        @ApplicationContext context: Context,
        vertexAI: VertexAI,
        generativeModel: GenerativeModel
    ): NeuralWhisper {
        return NeuralWhisper(context, vertexAI, generativeModel)
    }
}
