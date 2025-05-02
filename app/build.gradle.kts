import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias( com.google.dagger.hilt-android )

    id("com.google.devtools.ksp") version "1.9.22-1.0.16"
}

android {
    namespace = "dev.aurakai.auraframefx"
    compileSdk = 35

    defaultConfig {
        applicationId = "dev.aurakai.auraframefx"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildFeatures {
        compose = true
        buildConfig = true
        resValues = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                file("proguard-rules.pro")
            )
            signingConfig = signingConfigs.getByName("debug")
            isCrunchPngs = false
        }
        debug {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
            isCrunchPngs = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_22
        targetCompatibility = JavaVersion.VERSION_22
        isCoreLibraryDesugaringEnabled = true
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = "22" // Correct way to set jvmTarget
             freeCompilerArgs += listOf(
                    "-Xjvm-default=all",
                    "-opt-in=kotlin.RequiresOptIn",
                    "-Xallow-result-return-type"
                )
        }
    }

    packaging {
        resources {
            excludes += setOf("/META-INF/{AL2.0,LGPL2.1}")
        }
        jniLibs.useLegacyPackaging = true
    }
    buildToolsVersion = "35.0.0"
}