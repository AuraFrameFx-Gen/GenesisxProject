// Enable features
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots") }
    }
}

// Configure version catalogs
// Note: The libs.versions.toml file is automatically loaded in Gradle 8.4+
// No need for explicit versionCatalogs block

rootProject.name = "AuraFrameFX"

// Include the main app module
include(":app")