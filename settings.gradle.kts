import org.gradle.api.initialization.resolve.RepositoriesMode.*
import org.gradle.api.initialization.resolve.RepositoriesMode.PREFER_SETTINGS as PREFER_SETTINGS1

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
    plugins {
        id("com.android.application") version "8.2.2"
        id("com.android.library") version "8.2.2"
        id("org.jetbrains.kotlin.android") version "2.1.20"
        id("com.google.dagger.hilt.android") version "2.56.2"
        id("androidx.navigation.safeargs.kotlin") version "2.8.9"
    }
}

dependencyResolutionManagement {
    this.repositoriesMode.set(/* value = */ PREFER_SETTINGS1)
    this.repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "AuraFrameFX"
include(":app")