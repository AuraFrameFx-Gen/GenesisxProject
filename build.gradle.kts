plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.navigation.safe.args) apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.16"
}

repositories {
    google()
    mavenCentral()
}

kotlin {
    jvmToolchain(17)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(17))
    }
}

tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}