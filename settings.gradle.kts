pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}

@Suppress("UnstableApiUsage")
dependencyResolutionManagement {
    repositoriesMode = RepositoriesMode.FAIL_ON_PROJECT_REPOS
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }

    versionCatalogs {
        create("libs") {
            from(files("gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "AuraFrameFX"

// Include all modules
include(":app")
include(":XposedBridge-art")
include(":visualization")

// Set project directories
project(":XposedBridge-art").projectDir = file("XposedBridge-art")
project(":visualization").projectDir = file("visualization")