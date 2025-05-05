// Basic settings.gradle.kts configuration
rootProject.name = "AuraFrameFX"

// Include the app module
include(":app")

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