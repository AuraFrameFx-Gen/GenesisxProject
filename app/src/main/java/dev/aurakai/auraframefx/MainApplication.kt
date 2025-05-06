package dev.aurakai.auraframefx

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.WorkManager
import androidx.work.WorkerFactory
import dagger.hilt.android.HiltAndroidApp
import dev.aurakai.auraframefx.di.AppInitializer
import timber.log.Timber
import javax.inject.Inject

/**
 * Main application class for AuraFrameFX.
 * This class is the entry point for Hilt dependency injection and application-level configurations.
 */
@HiltAndroidApp
class MainApplication : Application() {

    @Inject
    lateinit var appInitializer: AppInitializer

    @Inject
    lateinit var workManager: WorkManager

    @Inject
    lateinit var workManagerConfig: Configuration

    @Inject
    lateinit var hiltWorkerFactory: HiltWorkerFactory

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)
        // Initialize any multi-dex or other base context requirements here
    }

    override fun onCreate() {
        super.onCreate()
        
        // Initialize Timber for logging
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        
        // Initialize app components
        appInitializer.initialize(this)
        
        // Trigger WorkManager initialization if not already done
        workManager
        
        Timber.d("Application initialization completed")
    }
}
