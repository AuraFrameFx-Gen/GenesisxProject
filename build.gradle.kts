// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.plugins.agp.get())
        classpath(libs.plugins.kotlin.android)
        classpath(libs.plugins.kotlin.parcelize)
        classpath(libs.plugins.ksp)
    }
}

// Configure all projects
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

// Configure all projects
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}