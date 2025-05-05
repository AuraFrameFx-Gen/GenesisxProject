pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }

    // Configure plugin resolution strategy
    resolutionStrategy {
        eachPlugin {
            when (requested.id.namespace) {
                "com.android" -> useModule("com.android.tools.build:gradle:${requested.version}")
                "org.jetbrains.kotlin" -> useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
                "com.google.dagger.hilt.android" -> useModule("com.google.dagger:hilt-android-gradle-plugin:${requested.version}")
            }
        }
    }
}

// Enable type-safe project accessors
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// Enable version catalogs
enableFeaturePreview("VERSION_CATALOGS")

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)

    // Configure repositories for all projects
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://api.xposed.info/") }
    }
}

// Version catalog is now defined in gradle/libs.versions.toml
// and automatically loaded by Gradle

rootProject.name = "AuraFrameFX"

// Core application module
include(":app")
// Dynamic feature modules
include(":visualization")
// Library modules
include(":XposedBridge-art")
