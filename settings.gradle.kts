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
        google() // Main Google repository
        mavenCentral()
        
        // Add JCenter which might be needed for some older dependencies
        jcenter() // Warning: JCenter is deprecated but might be needed for certain libraries
        
        // Specific repositories for Firebase VertexAI
        maven { url = uri("https://maven.google.com") } // Google Maven repository
        maven { url = uri("https://maven.scijava.org/content/repositories/public") }
        maven { url = uri("https://jitpack.io") } // For GitHub dependencies
    }
}

rootProject.name = "Beta-Build"

include(":app")