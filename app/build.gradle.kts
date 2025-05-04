plugins {
    id("com.android.application") version "8.8.2" apply true
    id("org.jetbrains.kotlin.android") version "1.9.20" apply true
    id("com.google.devtools.ksp") version "1.9.20-1.0.14" apply true
    id("com.google.dagger.hilt.android") version "2.56.2" apply true
}

android {
    namespace = "dev.aurakai.auraframefx"
    compileSdk = 34
    buildToolsVersion = "34.0.0"

    defaultConfig {
        applicationId = "dev.aurakai.auraframefx"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        proguardFiles("proguard-rules.pro")
        androidResources {
            generateLocaleConfig = true
        }
        multiDexEnabled = true
    }

    configurations.all {
        resolutionStrategy {
            force("org.jetbrains.kotlin:kotlin-stdlib:1.9.20")
            force("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.9.20")
            force("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.9.20")
        }
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = listOf(
            "-opt-in=kotlin.RequiresOptIn",
            "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            "-opt-in=kotlinx.coroutines.FlowPreview",
            "-opt-in=kotlinx.serialization.ExperimentalSerializationApi"
        )
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    buildFeatures {
        compose = true
        buildConfig = true
    }

    packaging {
        resources {
            excludes += setOf(
                "META-INF/**",
                "**/*.kotlin_module",
                "**/*.proto",
                "**/*.txt",
                "**/*.md",
                "**/*.xml",
                "**/*.properties"
            )
        }
        jniLibs {
            useLegacyPackaging = true
        }
        resources.pickFirsts += setOf(
            "**/version.txt",
            "**/version.properties"
        )
    }
}

dependencies {
    // Compose BOM
    implementation(platform(libs.androidx.compose.bom))

    // UI Components
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    debugImplementation(libs.androidx.ui.tooling)

    // Core Android
    debugImplementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.activity.compose)

    // Dependency Injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.core)

    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:1.8.1")

    // Google Cloud Services (Reduced set)
    implementation(platform("com.google.cloud:libraries-bom:2.24.0"))
    implementation("com.google.cloud:google-cloud-run:2.0.1")
    implementation("com.google.cloud:google-cloud-auth:2.0.1")
    implementation("com.google.cloud:google-cloud-storage:2.0.1")

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Desugaring
    coreLibraryDesugaring(libs.android.desugar.jdk.libs)
}