buildscript {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("com.android.application") version "8.2.2" apply false
    id("com.android.library") version "8.2.2" apply false
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.17" apply false
    id("com.google.dagger.hilt.android") version "2.56.2" apply false
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

// Clean build directory
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

// Enable configuration cache
tasks.withType<org.gradle.api.tasks.compile.JavaCompile>().configureEach {
    options.release.set(17)
}

// Enable parallel execution
gradle.startParameter.parallelThreadCount = Runtime.getRuntime().availableProcessors()

// Enable build caching
gradle.startParameter.isConfigurationCache = true

// Enable configuration on demand
gradle.startParameter.isConfigureOnDemand = true
