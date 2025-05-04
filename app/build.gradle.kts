plugins {
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.20"
}

ksp {
    arg("room.schemaLocation", "$projectDir/schemas")
    arg("room.incremental", "true")
}

android {
    namespace = "dev.aurakai.auraframefx"
    compileSdk = 35
    buildToolsVersion = "36.0.3"

    defaultConfig {
        applicationId = "dev.aurakai.auraframefx"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables.useSupportLibrary = true
        signingConfig = signingConfigs.getByName("debug")
        proguardFiles("proguard-rules.pro")
        androidResources.generateLocaleConfig = true
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("release")
        }
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf(
            "-Xuse-k2",
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=kotlin.Experimental",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=kotlinx.coroutines.FlowPreview",
            "-opt-in=kotlinx.coroutines.DelicateCoroutinesApi",
            "-P=plugin:androidx.compose.compiler.plugins.kotlin:suppressKotlinVersionCompatibilityCheck=true"
        )
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3" // Match your Kotlin version (see below)
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources.excludes += setOf(
            "/META-INF/{AL2.0,LGPL2.1}",
            "**/attach_hotspot_windows.dll"
        )
        jniLibs.useLegacyPackaging = true
    }
}

dependencies {
    // Compose BOM (version management)
    implementation(platform(libs.androidx.compose.bom))

    // Material 3
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.material.icons.extended)

    // Core Android
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity.compose)

    // Compose
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Serialization
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.protobuf)
    implementation(libs.kotlinx.serialization.cbor)

    // Navigation
    implementation(libs.navigation.compose)

    // Google Cloud
    implementation(platform(libs.google.cloud.bom))
    implementation("com.google.cloud:google-cloud-run")
    implementation("com.google.cloud:google-cloud-auth")
    implementation("com.google.cloud:google-cloud-storage")
    implementation("com.google.cloud:google-cloud-logging")
    implementation("com.google.cloud:google-cloud-monitoring")
    implementation("com.google.cloud:google-cloud-trace")
    implementation("com.google.cloud:google-cloud-vertexai")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Desugaring
    coreLibraryDesugaring(libs.android.desugar.jdk.libs)
}
