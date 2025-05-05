// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    // Core plugins
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.dynamicFeature) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.ksp) apply false
    
    // Dependency management plugins
    id("com.github.ben-manes.versions") version "0.47.0"
}

// Apply common build script configurations
apply(from = "gradle/dependencyConstraints.gradle.kts")
apply(from = "gradle/dependencyUpdates.gradle.kts")

// Configure all projects including the root project
allprojects {
    // Apply common configuration
    plugins.withType(JavaPlugin::class) {
        configure<JavaPluginExtension> {
            sourceCompatibility = JavaVersion.VERSION_17
            targetCompatibility = JavaVersion.VERSION_17
        }
    }
    
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://api.xposed.info/") }
    }
    
    // Apply common configurations to all subprojects (excluding the root project)
    if (this != rootProject) {
        apply(plugin = "org.jetbrains.kotlin.android")
        
        // Configure Kotlin options for all subprojects
        tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
            kotlinOptions {
                jvmTarget = JavaVersion.VERSION_17.toString()
                freeCompilerArgs = freeCompilerArgs + listOf(
                    "-Xcontext-receivers",
                    "-opt-in=kotlin.RequiresOptIn"
                )
            }
        }
        
        // Configure Java compilation for all subprojects
        tasks.withType<JavaCompile>().configureEach {
            sourceCompatibility = JavaVersion.VERSION_17.toString()
            targetCompatibility = JavaVersion.VERSION_17.toString()
            options.encoding = "UTF-8"
            options.isFork = true
        }
    }
}

// Clean task for the root project
tasks.register("clean", Delete::class) {
    delete(rootProject.layout.buildDirectory)
}

// Configure dependency resolution strategy for all configurations
configurations.all {
    // Enable dependency locking
    resolutionStrategy.activateDependencyLocking()
    
    // Fail on version conflicts
    resolutionStrategy.failOnVersionConflict()
    
    // Prefer project modules over external dependencies
    resolutionStrategy.preferProjectModules()
    
    // Cache dynamic versions for 10 minutes
    resolutionStrategy.cacheDynamicVersionsFor(10, "minutes")
    
    // Cache changing modules for 10 minutes
    resolutionStrategy.cacheChangingModulesFor(10, "minutes")
}

// Task to generate dependency lock files for all configurations
tasks.register("resolveAndLockAll") {
    doFirst {
        require(gradle.startParameter.isWriteDependencyLocks) {
            "This task requires the --write-locks flag to be set"
        }
    }
    
    doLast {
        configurations.filter { it.isCanBeResolved }.forEach { config ->
            try {
                // Copy and resolve the configuration to trigger dependency resolution
                val copy = config.copyRecursive()
                copy.setCanBeResolved(true)
                copy.resolve()
                
                // Log the configuration name for debugging
                logger.lifecycle("Resolved configuration: ${config.name}")
            } catch (e: Exception) {
                logger.warn("Failed to resolve configuration ${config.name}: ${e.message}")
            }
        }
    }
}

// Alias for resolveAndLockAll
tasks.register("lockAll") {
    dependsOn("resolveAndLockAll")
    group = "verification"
    description = "Resolves and locks all dependencies"
}
