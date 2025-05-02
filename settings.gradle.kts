import org.gradle.api.initialization.resolve.RepositoriesMode.*

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
    plugins {
        id("com.android.application") version "8.8.2" apply false
        id("com.android.library") version "8.8.2" apply false
        id("org.jetbrains.kotlin.android") version "1.9.22" apply false
        id("com.google.dagger.hilt.android") version "2.56.2" apply false
        id("androidx.navigation.safeargs.kotlin") version "2.7.6" apply false
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(PREFER_SETTINGS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "AuraFrameFX"
include(":app")