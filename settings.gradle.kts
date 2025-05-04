pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }

    // Configure version catalog
    includeBuild("gradle")
    
    plugins {
        id("com.android.application") version "8.8.2" apply false
        id("com.android.library") version "8.8.2" apply false
        id("org.jetbrains.kotlin.android") version "1.9.20" apply false
        id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply false
        id("com.google.dagger.hilt.android") version "2.56.2" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "AuraFrameFX"
include(
    ":app",
    ":visualization",
    ":XposedBridge-art"
)


// Enable configuration on demand
gradle.startParameter.isConfigureOnDemand = true
