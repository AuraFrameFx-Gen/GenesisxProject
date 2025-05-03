plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
}

repositories {
    google()
    mavenCentral()
    maven { url = uri("https://jitpack.io") }
}

tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}
