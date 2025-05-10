package dev.aurakai.auraframefx.ui.security

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dev.aurakai.auraframefx.ai.KaiController
import dev.aurakai.auraframefx.ui.components.KaiNotchBar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * ViewModel for managing Kai security toolbox settings
 */
class KaiToolboxViewModel @Inject constructor(
    private val kaiController: KaiController? = null
) : ViewModel() {

    // Toggle states for security features
    private val _adBlockingEnabled = mutableStateOf(false)
    val adBlockingEnabled: State<Boolean> = _adBlockingEnabled

    private val _ramOptimizationEnabled = mutableStateOf(false)
    val ramOptimizationEnabled: State<Boolean> = _ramOptimizationEnabled

    private val _systemMonitoringEnabled = mutableStateOf(false)
    val systemMonitoringEnabled: State<Boolean> = _systemMonitoringEnabled

    private val _errorCheckingEnabled = mutableStateOf(false)
    val errorCheckingEnabled: State<Boolean> = _errorCheckingEnabled

    // Notch position (0.0 = top, 1.0 = bottom)
    private val _notchPosition = mutableStateOf(0.0f)
    val notchPosition: State<Float> = _notchPosition
    
    // Ad blocking host list
    private val _adBlockingHosts = mutableStateOf<List<String>>(
        listOf(
            "ads.example.com",
            "tracker.analytics.com",
            "metrics.tracking.org"
        )
    )
    val adBlockingHosts: State<List<String>> = _adBlockingHosts

    private val coroutineScope = CoroutineScope(Dispatchers.IO)

    init {
        // Initialize with current settings from KaiNotchBar if available
        kaiController?.getKaiNotchBar()?.let { kaiNotchBar ->
            _adBlockingEnabled.value = kaiNotchBar.adBlockEnabled
            _ramOptimizationEnabled.value = kaiNotchBar.ramOptimizationEnabled
            _systemMonitoringEnabled.value = kaiNotchBar.systemMonitoringEnabled
            _errorCheckingEnabled.value = kaiNotchBar.errorCheckingEnabled
            _notchPosition.value = kaiNotchBar.notchPosition
            _adBlockingHosts.value = kaiNotchBar.blockedHosts.toList()
        } ?: run {
            Timber.d("KaiNotchBar not available, using default settings")
        }
    }

    fun updateAdBlocking(enabled: Boolean) {
        _adBlockingEnabled.value = enabled
        kaiController?.getKaiNotchBar()?.adBlockEnabled = enabled
        
        if (enabled) {
            kaiController?.getKaiNotchBar()?.startAdBlocker()
        } else {
            kaiController?.getKaiNotchBar()?.stopAdBlocker()
        }
    }

    fun updateRamOptimization(enabled: Boolean) {
        _ramOptimizationEnabled.value = enabled
        kaiController?.getKaiNotchBar()?.ramOptimizationEnabled = enabled
        
        if (enabled) {
            kaiController?.getKaiNotchBar()?.startRamOptimizer()
        } else {
            kaiController?.getKaiNotchBar()?.stopRamOptimizer()
        }
    }

    fun updateSystemMonitoring(enabled: Boolean) {
        _systemMonitoringEnabled.value = enabled
        kaiController?.getKaiNotchBar()?.systemMonitoringEnabled = enabled
        
        if (enabled) {
            kaiController?.getKaiNotchBar()?.startSystemMonitor()
        } else {
            kaiController?.getKaiNotchBar()?.stopSystemMonitor()
        }
    }

    fun updateErrorChecking(enabled: Boolean) {
        _errorCheckingEnabled.value = enabled
        kaiController?.getKaiNotchBar()?.errorCheckingEnabled = enabled
        
        if (enabled) {
            kaiController?.getKaiNotchBar()?.startErrorChecker()
        } else {
            kaiController?.getKaiNotchBar()?.stopErrorChecker()
        }
    }
    
    fun updateNotchPosition(position: Float) {
        _notchPosition.value = position
        kaiController?.getKaiNotchBar()?.notchPosition = position
        kaiController?.getKaiNotchBar()?.updateNotchPosition()
    }
    
    fun addHostToBlockList(host: String) {
        if (host.isNotEmpty()) {
            val currentList = _adBlockingHosts.value.toMutableList()
            if (!currentList.contains(host)) {
                currentList.add(host)
                _adBlockingHosts.value = currentList
                
                kaiController?.getKaiNotchBar()?.let { kaiNotchBar ->
                    kaiNotchBar.blockedHosts.add(host)
                    
                    if (kaiNotchBar.adBlockEnabled) {
                        kaiNotchBar.updateAdBlocker()
                    }
                }
            }
        }
    }
    
    fun removeHostFromBlockList(host: String) {
        val currentList = _adBlockingHosts.value.toMutableList()
        if (currentList.contains(host)) {
            currentList.remove(host)
            _adBlockingHosts.value = currentList
            
            kaiController?.getKaiNotchBar()?.let { kaiNotchBar ->
                kaiNotchBar.blockedHosts.remove(host)
                
                if (kaiNotchBar.adBlockEnabled) {
                    kaiNotchBar.updateAdBlocker()
                }
            }
        }
    }
}
