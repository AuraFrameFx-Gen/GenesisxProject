buildscript {
    dependencies {
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.50") // Or the correct version
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.navigation.safe.args) apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.16"
}
tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}