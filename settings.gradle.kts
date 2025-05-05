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
    }
}

rootProject.name = "AuraFrameFX"
include(":app")

// Configure all projects
allprojects {
    group = "dev.aurakai.auraframefx"
    version = "1.0.0"
}

// Include core modules
include(":core:common")
include(":core:database")
include(":core:network")

// Include feature modules
include(":libraries:analytics")
include(":libraries:crashreporting")

// Configure repositories for all projects
allprojects {
    repositories {
        google()
        mavenCentral()
    }
}