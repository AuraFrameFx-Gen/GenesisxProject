package dev.aurakai.auraframefx.ai

/**
 * Class to hold security-related information that can be exchanged
 * between Neural Whisper (Aura) and Kai
 */
data class SecurityContext(
    val adBlockingActive: Boolean = false,
    val ramUsage: Double = 0.0,
    val cpuUsage: Double = 0.0,
    val batteryTemp: Double = 0.0,
    val recentErrors: Int = 0
) {
    /**
     * Determine if there are any active security concerns
     */
    fun hasSecurityConcerns(): Boolean {
        return ramUsage > 80.0 || cpuUsage > 85.0 || batteryTemp > 40.0 || recentErrors > 0
    }
    
    /**
     * Get a human-readable description of security concerns
     */
    fun getSecurityConcernsDescription(): String {
        val concerns = mutableListOf<String>()
        
        if (ramUsage > 80.0) {
            concerns.add("High RAM usage (${ramUsage.toInt()}%)")
        }
        
        if (cpuUsage > 85.0) {
            concerns.add("High CPU usage (${cpuUsage.toInt()}%)")
        }
        
        if (batteryTemp > 40.0) {
            concerns.add("Elevated battery temperature (${batteryTemp.toInt()}°C)")
        }
        
        if (recentErrors > 0) {
            concerns.add("$recentErrors recent error events")
        }
        
        return if (concerns.isEmpty()) {
            "No security concerns detected"
        } else {
            "Security concerns: ${concerns.joinToString(", ")}"
        }
    }
}
