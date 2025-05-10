package dev.aurakai.auraframefx.ui.security

import dev.aurakai.auraframefx.ai.KaiController
import dev.aurakai.auraframefx.ui.components.KaiNotchBar
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class KaiToolboxViewModelTest {

    @Mock
    private lateinit var mockKaiController: KaiController
    
    @Mock
    private lateinit var mockKaiNotchBar: KaiNotchBar
    
    private lateinit var viewModel: KaiToolboxViewModel
    
    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        `when`(mockKaiController.getKaiNotchBar()).thenReturn(mockKaiNotchBar)
        
        viewModel = KaiToolboxViewModel(mockKaiController)
    }
    
    @Test
    fun `test update ad blocking`() {
        // Given
        val enabled = true
        
        // When
        viewModel.updateAdBlocking(enabled)
        
        // Then
        verify(mockKaiNotchBar).adBlockEnabled = enabled
        verify(mockKaiNotchBar).startAdBlocker()
    }
    
    @Test
    fun `test update ram optimization`() {
        // Given
        val enabled = true
        
        // When
        viewModel.updateRamOptimization(enabled)
        
        // Then
        verify(mockKaiNotchBar).ramOptimizationEnabled = enabled
        verify(mockKaiNotchBar).startRamOptimizer()
    }
    
    @Test
    fun `test update notch position`() {
        // Given
        val position = 0.5f
        
        // When
        viewModel.updateNotchPosition(position)
        
        // Then
        verify(mockKaiNotchBar).notchPosition = position
        verify(mockKaiNotchBar).updateNotchPosition()
    }
    
    @Test
    fun `test add host to block list`() {
        // Given
        val host = "ads.example.com"
        `when`(mockKaiNotchBar.adBlockEnabled).thenReturn(true)
        `when`(mockKaiNotchBar.blockedHosts).thenReturn(mutableSetOf())
        
        // When
        viewModel.addHostToBlockList(host)
        
        // Then
        verify(mockKaiNotchBar.blockedHosts).add(host)
        verify(mockKaiNotchBar).updateAdBlocker()
    }
}
