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
    compileSdk = 34

    defaultConfig {
        applicationId = "dev.aurakai.auraframefx"
        minSdk = 31
        targetSdk = 34
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

        externalNativeBuild {
            cmake {
                path = file("src/main/cpp/CMakeLists.txt")
                version = "3.22.1"
                cppFlags.addAll(listOf(
                    "-std=c++17",
                    "-fexceptions",
                    "-frtti"
                ))
                arguments.addAll(listOf(
                    "-DANDROID_STL=c++_shared",
                    "-DANDROID_ARM_NEON=TRUE"
                ))
            }
        }

        ksp {
            arg("room.schemaLocation", "$projectDir/schemas")
            arg("room.incremental", "true")
        }
    }

    signingConfigs {
        if (keystoreProperties.getProperty("keyAlias") != null) {
            create("release") {
                keyAlias = keystoreProperties.getProperty("keyAlias")
                keyPassword = keystoreProperties.getProperty("keyPassword")
                setStoreFile(rootProject.file(keystoreProperties.getProperty("storeFile", "keystore/default.jks")))
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
        }
        create("foss") {
            dimension = "distribution"
            applicationIdSuffix = ".foss"
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
        kotlinCompilerExtensionVersion = libs.versions.composeCompiler.get()
    }

    packaging {
        resources {
            excludes.addAll(listOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE*",
                "META-INF/NOTICE*",
                "META-INF/ASL2.0",
                "META-INF/*.kotlin_module",
                "META-INF/versions/9/previous-compilation-data.bin",
                "**/attach_hotspot_api.dll",
                "DebugProbesKt.bin"
            ))
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
    coreLibraryDesugaring(libs.desugar.jdk.libs)

    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.analytics.ktx)
    implementation(libs.firebase.firestore.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.messaging.ktx)
    implementation(libs.firebase.vertexai.ktx)
    implementation(libs.google.play.services.auth)
    implementation(libs.google.generativeai)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.multidex)
    implementation(libs.androidx.startup.runtime)
    implementation(libs.androidx.work.runtime.ktx)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.androidx.palette.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.viewpager2)

    implementation(libs.material)

    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)

    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)

    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.work)
    implementation(libs.hilt.navigation.compose)

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)

    implementation(libs.okhttp)
    implementation(libs.okhttp.logging.interceptor)
    implementation(libs.retrofit)
    implementation(libs.retrofit.converter.gson)
    implementation(libs.gson)

    implementation(libs.coil.compose)
    ksp(libs.glide.ksp)
    implementation(libs.glide.okhttp.integration)

    implementation(libs.lottie.compose)
    implementation(libs.jsoup)
    implementation(libs.zip4j)
    implementation(libs.timber)
    implementation(libs.bouncycastle.prov)

    compileOnly(libs.xposed.bridge)
    compileOnly("com.github.deltazefiro:XposedBridge:${libs.versions.xposedBridgeDeltazefiro.get()}:sources")


    implementation(libs.libsu.core)
    implementation(libs.libsu.service)
    implementation(libs.libsu.nio)

    implementation(libs.remotepreferences)

    testImplementation(libs.junit)
    testImplementation(libs.mockk.android)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.test.core)
    testImplementation(libs.androidx.arch.core.testing)

    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    androidTestImplementation(libs.hilt.android.testing)
    kaptAndroidTest(libs.hilt.compiler)
}