pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    }

    versionCatalogs {
        create("libs") {
            from(files("gradle/libs.versions.toml"))
        }
    }
}

// Enable Gradle's version catalog support
enableFeaturePreview("VERSION_CATALOGS")

rootProject.name = "AuraFrameFX"

// Include all modules
include(":app")

// Function to include modules with proper path handling
fun includeWithPath(modulePath: String, projectDir: String) {
    include(modulePath)
    project(modulePath).projectDir = file(projectDir)
}

// Include all feature modules
val featureModules = listOf(
    "feature:home",
    "feature:settings",
    "feature:about"
    // Add more feature modules here as needed
)

featureModules.forEach { module ->
    val (type, name) = module.split(":")
    includeWithPath(":$type-$name", "$type/$name")
}

// Include core modules
include(":core:network")
include(":core:database")
include(":core:common")

// Include other modules
include(":libraries:analytics")
include(":libraries:crashreporting")

// Configure build cache
buildCache {
    local {
        // Local build cache configuration
        isEnabled = true
        directory = File(rootDir, "build-cache")
    }
}

// Configure all projects
allprojects {
    // Configure project properties
    group = "dev.aurakai.auraframefx"
    version = "1.0.0"

    // Apply common configurations to all projects
    afterEvaluate {
        // Configure common properties for all projects
        if (plugins.hasPlugin("com.android.application") ||
            plugins.hasPlugin("com.android.library")
        ) {

            // Configure Android specific settings
            extensions.configure<com.android.build.gradle.BaseExtension> {
                // Common Android configurations
                compileSdkVersion(34)

                defaultConfig {
                    minSdk = 21
                    targetSdk = 34
                    versionCode = 1
                    versionName = "1.0.0"
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }

                kotlinOptions {
                    jvmTarget = JavaVersion.VERSION_17.toString()
                }
            }
        }
    }
}