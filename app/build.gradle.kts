import java.io.FileInputStream
import java.util.Properties

val keystorePropertiesFile = rootProject.file("keystore.properties")
val keystoreProperties = Properties().apply {
    if (keystorePropertiesFile.exists()) {
        try {
            load(FileInputStream(keystorePropertiesFile))
        } catch (e: Exception) {
            println("Warning: Failed to load keystore.properties: ${e.message}")
        }
    }
}

plugins {
    alias(libs.plugins.agp.app)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.gms)
    id("kotlin-kapt")
}

val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")

android {
    namespace = "dev.aurakai.auraframefx"
    compileSdk = 35

    defaultConfig {
        externalNativeBuild {
            cmake {
                cppFlags.addAll(listOf("-std=c++17", "-fexceptions", "-frtti"))
                arguments.addAll(listOf("-DANDROID_STL=c++_shared", "-DANDROID_ARM_NEON=TRUE"))
            }
        }
        applicationId = "dev.aurakai.auraframefx"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
        multiDexEnabled = true
        ndk {
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
        }
        signingConfig = signingConfigs.release
    }

    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }

    ksp {
        arg("room.schemaLocation", "$projectDir/schemas")
        arg("room.incremental", "true")
    }

    // Signing configuration using keystore.properties
    signingConfigs {
        if (keystoreProperties.getProperty("keyAlias") != null) {
            create("release") {
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
                storeFile = rootProject.file(keystoreProperties.getProperty("storeFile", "keystore/auraframefx-release.jks"))
                storePassword = keystoreProperties.getProperty("storePassword")
            }
        }
        getByName("debug") {}
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            applicationIdSuffix = ".debug"
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.findByName("release")
        }
    }

    flavorDimensions.add("distribution")
    productFlavors {
        create("standard") {
            dimension = "distribution"
            // Optionally set:
            // minSdk = 31
            // versionName = "1.0.0"
        }
        create("foss") {
            dimension = "distribution"
            applicationIdSuffix = ".foss"
            versionCode = 1
            proguardFiles("proguard-rules.pro")
            versionName = "beta"
        }
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/java", "src/main/xposed")
            res.srcDirs("src/main/res")
            assets.srcDirs("src/main/assets")
            jniLibs.srcDirs("src/main/jniLibs")
        }
        findByName("standard")?.java?.srcDirs("src/standard/java")
        findByName("foss")?.java?.srcDirs("src/foss/java")

    }

    splits {
        abi {
            isEnable = true
            reset()
            include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
            isUniversalApk = true
        }
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
        compose = true
        aidl = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.findVersion("composeCompiler").get().toString()
    }

    packaging {
        resources {
            excludes.addAll(
                listOf(
                    "/META-INF/{AL2.0,LGPL2.1}",
                    "META-INF/DEPENDENCIES",
                    "META-INF/LICENSE*",
                    "META-INF/NOTICE*",
                    "META-INF/ASL2.0",
                    "META-INF/*.kotlin_module",
                    "META-INF/versions/9/previous-compilation-data.bin",
                    "**/attach_hotspot_api.dll",
                    "DebugProbesKt.bin"
                )
            )
        }
        jniLibs {
            pickFirsts.add("**/libc++_shared.so")
            useLegacyPackaging = false
        }
    }

    ndkVersion = "26.1.10909125"

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }

    compileOptions {
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs += listOf(
            "-Xjvm-default=all",
            "-opt-in=kotlin.RequiresOptIn"
        )
    }
}

dependencies {
    coreLibraryDesugaring(libs.findLibrary("desugar-jdk-libs").get())

    // Firebase
    implementation(platform(libs.findLibrary("firebase-bom").get()))
    implementation(libs.findLibrary("firebase-analytics-ktx").get())
    implementation(libs.findLibrary("firebase-firestore-ktx").get())
    implementation(libs.findLibrary("firebase-auth-ktx").get())
    implementation(libs.findLibrary("firebase-storage-ktx").get())
    implementation(libs.findLibrary("firebase-messaging-ktx").get())
    implementation(libs.findLibrary("google-play-services-auth").get())
    implementation(libs.findLibrary("google-generativeai").get())

    // AndroidX
    // Ensures core Android classes like Service, Intent, IBinder are available
    implementation(libs.findLibrary("androidx-core-ktx").get())
    implementation(libs.findLibrary("androidx-activity-ktx").get())
    // Ensures backward compatibility for Android components
    implementation(libs.findLibrary("androidx-appcompat").get())
    implementation(libs.findLibrary("androidx-constraintlayout").get())

    implementation(libs.findLibrary("androidx-multidex").get())
    implementation(libs.findLibrary("androidx-startup-runtime").get())
    implementation(libs.findLibrary("androidx-work-runtime-ktx").get())
    implementation(libs.findLibrary("androidx-preference-ktx").get())
    implementation(libs.findLibrary("androidx-palette-ktx").get())
    implementation(libs.findLibrary("androidx-recyclerview").get())
    implementation(libs.findLibrary("androidx-viewpager2").get())
    implementation(libs.findLibrary("material").get())

    // Compose
    implementation(platform(libs.findLibrary("androidx-compose-bom").get()))
    implementation(libs.findLibrary("androidx-compose-ui").get())
    implementation(libs.findLibrary("androidx-compose-material3").get())
    implementation(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
    debugImplementation(libs.findLibrary("androidx-compose-ui-tooling").get())
    implementation(libs.findLibrary("androidx-activity-compose").get())
    implementation(libs.findLibrary("androidx-lifecycle-runtime-ktx").get())
    implementation(libs.findLibrary("androidx-lifecycle-viewmodel-ktx").get())
    implementation(libs.findLibrary("androidx-lifecycle-livedata-ktx").get())
    implementation(libs.findLibrary("androidx-lifecycle-runtime-compose").get())
    implementation(libs.findLibrary("androidx-lifecycle-viewmodel-compose").get())
    implementation(libs.findLibrary("androidx-navigation-compose").get())

    // Room
    implementation(libs.findLibrary("androidx-room-runtime").get())
    implementation(libs.findLibrary("androidx-room-ktx").get())
    ksp(libs.findLibrary("androidx-room-compiler").get())

    // Hilt
    implementation(libs.findLibrary("hilt-android").get())
    kapt(libs.findLibrary("hilt-compiler").get())
    implementation(libs.findLibrary("hilt-work").get())
    implementation(libs.findLibrary("hilt-navigation-compose").get())

    // Kotlinx Coroutines
    implementation(libs.findLibrary("kotlinx-coroutines-core").get())
    implementation(libs.findLibrary("kotlinx-coroutines-android").get())

    // Networking
    implementation(libs.findLibrary("okhttp").get())
    implementation(libs.findLibrary("okhttp-logging-interceptor").get())
    implementation(libs.findLibrary("retrofit").get())
    implementation(libs.findLibrary("retrofit-converter-gson").get())
    implementation(libs.findLibrary("gson").get())

    // Images
    implementation(libs.findLibrary("coil-compose").get())
    ksp(libs.findLibrary("glide-ksp").get())
    implementation("com.github.bumptech.glide:okhttp3-integration:4.16.0")
    implementation(libs.findLibrary("lottie-compose").get())

    // Utilities
    implementation(libs.findLibrary("jsoup").get())
    implementation(libs.findLibrary("zip4j").get())
    implementation(libs.findLibrary("timber").get())
    implementation("org.bouncycastle:bcprov-jdk18on:1.80")

    // Xposed
    compileOnly(libs.findLibrary("xposed-bridge").get())
    compileOnly("com.github.deltazefiro:XposedBridge:${libs.findVersion("xposedBridgeDeltazefiro").get()}:sources")

    // libsu
    implementation(libs.findLibrary("libsu-core").get())
    implementation(libs.findLibrary("libsu-service").get())
    implementation(libs.findLibrary("libsu-nio").get())

    // Remote Preferences
    implementation(libs.findLibrary("remotepreferences").get())

    // Testing
    testImplementation(libs.findLibrary("junit").get())
    testImplementation(libs.findLibrary("mockk-android").get())
    testImplementation(libs.findLibrary("kotlinx-coroutines-test").get())
    testImplementation(libs.findLibrary("androidx-test-core").get())
    testImplementation(libs.findLibrary("androidx-arch-core-testing").get())

    androidTestImplementation(libs.findLibrary("androidx-test-ext-junit").get())
    androidTestImplementation(libs.findLibrary("espresso-core").get())
    androidTestImplementation(platform(libs.findLibrary("androidx-compose-bom").get()))
    androidTestImplementation(libs.findLibrary("androidx-compose-ui-test-junit4").get())
    debugImplementation(libs.findLibrary("androidx-compose-ui-test-manifest").get())
    androidTestImplementation(libs.findLibrary("hilt-android-testing").get())
    kaptAndroidTest(libs.findLibrary("hilt-compiler").get())
}

