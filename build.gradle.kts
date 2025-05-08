// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

    dependencies {
        classpath("com.android.tools.build:gradle:8.8.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.56.2")
        classpath("com.google.gms:google-services:4.4.2")
    }
}

plugins {
    alias(libs.plugins.agp.app) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.parcelize) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.gms) apply false
}

// Clean task to delete the build directory
tasks.register<Delete>("clean") {
    delete(rootProject.layout.buildDirectory)
}

// Configure all projects with common settings
allprojects {
    // Configure Kotlin compiler options for all projects
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "17"
            freeCompilerArgs = freeCompilerArgs + "-Xjvm-default=all"
            freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
        }
    }

    // Configure Java compilation options for all projects
    tasks.withType<JavaCompile>().configureEach {
        sourceCompatibility = JavaVersion.VERSION_17.toString()
        targetCompatibility = JavaVersion.VERSION_17.toString()
        options.release.set(17)
    }

    // Configure NDK version for all Android projects
    pluginManager.withPlugin("com.android.application") {
        project.extensions.getByType<com.android.build.gradle.BaseExtension>().apply {
            ndkVersion = "26.1.10909125"
        }
    }
}

// Task to print project dependencies (optional)
tasks.register("printDependencies") {
    group = "help"
    description = "Print project dependencies"
    doLast {
        rootProject.allprojects.forEach { project ->
            println("\nProject: ${project.name}")
            println("-".repeat(50))
            project.configurations.filter { it.isCanBeResolved }.forEach { configuration ->
                try {
                    println("\nConfiguration: ${configuration.name}")
                    configuration.resolvedConfiguration.resolvedArtifacts.forEach { artifact ->
                        println("- ${artifact.moduleVersion.id}")
                    }
                } catch (e: Exception) {
                    println("Error resolving configuration ${configuration.name}: ${e.message}")
                }
            }
        }
    }
}

