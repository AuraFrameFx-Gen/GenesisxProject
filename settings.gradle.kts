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
        google()  // This should include the Firebase artifacts
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        
        // Add the maven.google.com repository explicitly as an alternative
        maven { url = uri("https://maven.google.com") }
    }
}

rootProject.name = "Beta-Build"

include(":app")