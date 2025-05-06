plugins {
    alias(libs.plugins.android.dynamicFeature)
}

android {
    namespace = "dev.aurakai.auraframefx.xposed"
    compileSdk = 35
    buildToolsVersion = "35.0.0"

    defaultConfig {
        dimension = "feature"
        minSdk = 31
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")

        // Required when setting minSdk to less than 20
        multiDexEnabled = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
        isCoreLibraryDesugaringEnabled = true
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
        freeCompilerArgs = freeCompilerArgs + listOf(
            "-Xcontext-receivers",
            "-opt-in=kotlin.RequiresOptIn"
        )
    }

    buildFeatures {
        buildConfig = true
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "/META-INF/gradle/incremental.annotation.processors"
        }
    }
}

dependencies {
    // Xposed Framework API
    compileOnly("de.robv.android.xposed:api:82") {
        exclude(group = "com.android.support")
        exclude(group = "com.android.tools.build")
    }

    implementation("de.robv.android.xposed:bridge:82") {
        exclude(group = "com.android.support")
        exclude(group = "com.android.tools.build")
    }

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
}
