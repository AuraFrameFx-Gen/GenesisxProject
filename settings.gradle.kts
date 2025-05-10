pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        // Simplified repository configuration for Firebase dependencies
        maven { url = uri("https://maven.google.com") }
        // If needed for other dependencies
        maven { url = uri("https://jitpack.io") }
    }
}

rootProject.name = "Beta-Build"

include(":app")