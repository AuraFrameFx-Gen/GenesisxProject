import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.agp.app)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
    id("com.google.gms.google-services")
}

android {
    namespace = "dev.aurakai.auraframefx"
    compileSdk = 35

    // Compilation options
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    kotlinOptions {
        jvmTarget = "17"
        freeCompilerArgs = freeCompilerArgs + "-Xjvm-default=all"
        freeCompilerArgs = freeCompilerArgs + "-opt-in=kotlin.RequiresOptIn"
    }

    defaultConfig {
        applicationId = "dev.aurakai.auraframefx"
        minSdk = 31
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        setProperty("archivesBaseName", "AuraFrameFX-v${versionName}")
        buildConfigField("int", "MIN_SDK_VERSION", "$minSdk")

        // Enable multidex support
        multiDexEnabled = true

        // NDK configuration
        ndk {
            abiFilters.clear()
            abiFilters.addAll(listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64"))
            // Use a specific NDK version
            version = "26.1.10909125"
        }

        // External native build configuration
        externalNativeBuild {
            cmake {
                (file("C:\\Users\\Wehtt\\Desktop\\Beta-Build\\app\\rc\\main\\cpp\\CMakeLists.txt"))
                version = "3.22.1"
                cppFlags("-std=c++17", "-fexceptions", "-frtti")
                arguments(
                    "-DANDROID_STL=c++_shared",
                    "-DANDROID_ARM_NEON=TRUE",
                    "-DCMAKE_VERBOSE_MAKEFILE=ON"
                )
            }
        }

        // Enable prefab for native dependencies
        buildFeatures {
            prefab = true
        }

        // Room schema location for KSP
        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
            arg("room.incremental", "true")
            arg("room.expandProjection", "true")
        }
    }

    val keystorePropertiesFile = rootProject.file("keystore.properties")
    var releaseSigning = signingConfigs.getByName("debug")

    try {
        val keystoreProperties = Properties()
        FileInputStream(keystorePropertiesFile).use { inputStream ->
            keystoreProperties.load(inputStream)
        }

        releaseSigning = signingConfigs.create("release") {
            keyAlias = keystoreProperties.getProperty("keyAlias")
            keyPassword = keystoreProperties.getProperty("keyPassword")
            storeFile = rootProject.file(keystoreProperties.getProperty("storeFile"))
            storePassword = keystoreProperties.getProperty("storePassword")
        }
    } catch (ignored: Exception) {
    }

    buildTypes {
        debug {
            isDebuggable = true
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            applicationIdSuffix = ".debug"
            resValue("string", "derived_app_name", "AuraFrameFX (Debug)")
            signingConfig = releaseSigning
        }

        release {
            isMinifyEnabled = true
            isShrinkResources = true
            isCrunchPngs = false
            proguardFiles("proguard-android-optimize.txt", "proguard.pro", "proguard-rules.pro")
            resValue("string", "derived_app_name", "AuraFrameFX")
            signingConfig = releaseSigning
        }
    }

    flavorDimensions += "distribution"

    productFlavors {
        create("standard") {
            isDefault = true
            dimension = "distribution"
            resValue("string", "derived_app_name", "AuraFrameFX")
        }

        create("foss") {
            dimension = "distribution"
            applicationIdSuffix = ".foss"
            resValue("string", "derived_app_name", "AuraFrameFX (FOSS)")
        }
    }

    sourceSets {
        getByName("main") {
            java.srcDirs("src/main/java")
            res.srcDirs("src/main/res")
            assets.srcDirs("src/main/assets")
            jniLibs.srcDirs("src/main/jniLibs")
        }

        getByName("standard") {
            java.srcDirs("src/standard/java")
        }

        getByName("foss") {
            java.srcDirs("src/foss/java")
        }
    }

    if (hasProperty("splitApks")) {
        splits {
            abi {
                isEnable = true
                reset()
                include("arm64-v8a", "armeabi-v7a", "x86", "x86_64")
                isUniversalApk = false
            }
        }
    }

    dependenciesInfo {
        includeInApk = false
        includeInBundle = false
    }

    buildFeatures {
        buildConfig = true
        viewBinding = true
        aidl = true
        renderScript = false
        shaders = false
    }

    buildFeatures.compose = true
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
            excludes += "META-INF/DEPENDENCIES"
            excludes += "META-INF/INDEX.LIST"
            excludes += "META-INF/NOTICE"
            excludes += "META-INF/NOTICE.txt"
            excludes += "META-INF/notice.txt"
            excludes += "META-INF/LICENSE"
            excludes += "META-INF/LICENSE.txt"
            excludes += "META-INF/license.txt"
            excludes += "META-INF/ASL2.0"
            excludes += "META-INF/*.kotlin_module"
            excludes += "META-INF/*.version"
            excludes += "META-INF/proguard/*"
            excludes += "META-INF/services/*"
            excludes += "META-INF/licenses/*"
            excludes += "META-INF/versions/9/previous-compilation-data.bin"
        }
    }

    // Configure ABI splits
    splits {
        abi {
            isEnable = true
            reset()
            include("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
            isUniversalApk = true
        }
    }

    packaging {
        resources {
            excludes += listOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/DEPENDENCIES",
                "META-INF/INDEX.LIST",
                "META-INF/NOTICE",
                "META-INF/NOTICE.txt",
                "META-INF/notice.txt",
                "META-INF/LICENSE",
                "META-INF/LICENSE.txt",
                "META-INF/license.txt",
                "META-INF/ASL2.0",
                "META-INF/*.kotlin_module",
                "META-INF/*.version",
                "META-INF/proguard/*",
                "META-INF/services/*",
                "META-INF/licenses/*",
                "META-INF/versions/9/previous-compilation-data.bin",
                "/META-INF/*",
                "/META-INF/versions/**",
                "/org/bouncycastle/**",
                "/kotlin/**",
                "/kotlinx/**",
                "rebel.xml",
                "/*.txt",
                "/*.bin",
                "/*.json"
            )
            pickFirsts.add("**/libc++_shared.so")
        }
        jniLibs {
            useLegacyPackaging = true
        }
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
    buildToolsVersion = "35.0.0"
    ndkVersion = "26.1.10909125"
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Xlint:-deprecation")
}

gradle.taskGraph.whenReady {
    gradle.startParameter.showStacktrace = ShowStacktrace.ALWAYS
    gradle.startParameter.warningMode = WarningMode.Summary
}

gradle.taskGraph.whenReady {
    gradle.startParameter.showStacktrace = ShowStacktrace.ALWAYS
    gradle.startParameter.warningMode = WarningMode.Summary
}

val fossImplementation by configurations
val standardImplementation by configurations

dependencies {
    // Kotlin
    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.android)


    // AndroidX Core
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.startup.runtime)
    implementation(libs.androidx.work.runtime.ktx)

    // UI Components
    implementation("com.google.android.material:material:1.12.0")
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.viewpager2)
    implementation(libs.androidx.palette.ktx)
    implementation(libs.androidx.preference.ktx)

    // Navigation
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Lifecycle
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)

    // Room database
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    // Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)

    // Networking
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.gson)

    // Image Loading
    implementation(libs.glide)
    ksp(libs.glide.compiler)
    implementation("com.github.bumptech.glide:okhttp3-integration:4.16.0")

    // Third-party UI
    implementation(libs.lottie)
    implementation(libs.circleindicator)
    implementation(libs.flexbox)

    // HTML Parsing
    implementation(libs.jsoup)

    // Root & Xposed
    compileOnly("de.robv.android.xposed:api:${libs.versions.xposedApiVersion.get()}")
    compileOnly("de.robv.android.xposed:api:${libs.versions.xposedApiVersion.get()}:sources")
    implementation("com.github.topjohnwu.libsu:core:${libs.versions.libsuVersion.get()}")
    implementation("com.github.topjohnwu.libsu:service:${libs.versions.libsuVersion.get()}")
    implementation("com.github.topjohnwu.libsu:nio:${libs.versions.libsuVersion.get()}")

    // Security & Utils
    implementation("org.bouncycastle:bcprov-jdk15on:1.70")
    implementation(libs.zip4j)
    implementation(libs.timber)
    implementation(libs.threetenabp)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.arch.core.testing)

    // Android Testing
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.ext.truth)

    // Hilt testing
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.56.2")
    kspAndroidTest("com.google.dagger:hilt-compiler:2.56.2")
}

tasks.register("printVersionName") {
    println(android.defaultConfig.versionName?.replace("-(Stable|Beta)".toRegex(), ""))
}