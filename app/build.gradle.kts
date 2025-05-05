import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.agp.app)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.parcelize)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.ksp)
}

android {
    namespace = "dev.aurakai.auraframefx"
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.aurakai.auraframefx"
        minSdk = 31
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.0"

        setProperty("archivesBaseName", "AuraFrameFX-v${versionName}")
        buildConfigField("int", "MIN_SDK_VERSION", "$minSdk")

        // Enable multidex support
        multiDexEnabled = true

        // NDK configuration
        ndk {
            abiFilters += listOf("armeabi-v7a", "arm64-v8a", "x86", "x86_64")
        }

        // External native build configuration
        externalNativeBuild {
            cmake {
                cppFlags("-std=c++17")
                arguments(
                    "-DANDROID_STL=c++_shared",
                    "-DANDROID_ARM_NEON=TRUE"
                )
                version = "3.22.1"
            }
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

    externalNativeBuild {
        cmake {
            path = file("CMakeLists.txt")
            version = "3.22.1"
        }
    }

    ndkVersion = "25.2.9519653"

    sourceSets {
        getByName("main") {
            jniLibs.srcDirs = ['src/main/jniLibs']
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    kotlinOptions {
        jvmTarget = "17"
    }

    packaging {
        jniLibs.excludes += setOf(
            "/META-INF/*",
            "/META-INF/versions/**",
            "/org/bouncycastle/**",
            "/kotlin/**",
            "/kotlinx/**"
        )

        resources.excludes += setOf(
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

        jniLibs.useLegacyPackaging = true
    }

    lint {
        abortOnError = false
        checkReleaseBuilds = false
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.compilerArgs.add("-Xlint:-deprecation")
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

    // Data Binding
    implementation(libs.library)
    implementation(libs.androidx.palette.ktx)

    // Xposed API
    // F-Droid disallow `api.xposed.info` since it's not a "Trusted Maven Repository".
    // So we create a mirror GitHub repository and obtain the library from `jitpack.io` instead.
    // Equivalent to `implementation 'de.robv.android.xposed:api:82'`.
    compileOnly(libs.xposedbridge)

    // The core module that provides APIs to a shell
    implementation(libs.su.core)
    // Optional: APIs for creating root services. Depends on ":core"
    implementation(libs.su.service)
    // Optional: Provides remote file system support
    implementation(libs.su.nio)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)

    // Color Picker
    implementation(libs.jaredrummler.colorpicker)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Material Components
    implementation(libs.material)

    // APK Signer
    implementation(libs.bcpkix.jdk18on)

    // Zip Util
    implementation(libs.zip4j)

    // Preference
    implementation(libs.androidx.preference.ktx)

    // Remote Preference
    implementation(libs.remotepreferences)

    // Flexbox
    implementation(libs.flexbox)

    // Glide
    implementation(libs.glide)
    ksp(libs.glide.compiler)

    // RecyclerView
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.recyclerview.selection)

    // ViewPager2
    implementation(libs.androidx.viewpager2)

    // Circle Indicator
    implementation(libs.circleindicator)

    // Lottie Animation
    implementation(libs.lottie)

    // HTML Parser
    implementation(libs.jsoup)

    // Collapsing Toolbar with subtitle
    implementation(libs.collapsingtoolbarlayout.subtitle)

    // Navigation Component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Concurrency
    implementation(libs.androidx.work.runtime)
    implementation(libs.androidx.concurrent.futures)
    implementation(libs.guava)

    // Event Bus
    implementation(libs.eventbus)

    // Dots Indicator
    implementation(libs.dotsindicator)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.multidex)

    // Hilt for dependency injection
    implementation(libs.hilt.android)
    ksp(libs.hilt.compiler)
    implementation(libs.hilt.work)

    // Room database
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)

    // Coroutines
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.coroutines.core)

    // Lifecycle components
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.extensions)

    // Navigation component
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    // Compose
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.runtime.livedata)
    debugImplementation(libs.androidx.compose.ui.tooling)

    // Xposed framework
    compileOnly(libs.xposed.api)

    // Image loading and processing
    implementation(libs.glide)
    ksp(libs.glide.compiler)
    implementation(libs.glide.transformations)

    // Networking
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)

    // JSON processing
    implementation(libs.gson)

    // Utility libraries
    implementation(libs.timber)
    implementation(libs.threetenabp)
    
    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.arch.core.testing)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    androidTestImplementation(libs.androidx.test.runner)
    androidTestImplementation(libs.androidx.test.rules)
    androidTestImplementation(libs.androidx.test.ext.truth)

    // For instrumented tests
    androidTestImplementation(libs.hilt.android.testing)
    kspAndroidTest(libs.hilt.compiler)
}

tasks.register("printVersionName") {
    println(android.defaultConfig.versionName?.replace("-(Stable|Beta)".toRegex(), ""))
}