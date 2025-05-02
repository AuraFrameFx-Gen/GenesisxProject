import org.gradle.kotlin.dsl.dependencies as dependencies1

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.navigation.safe.args) apply false
    id("com.google.devtools.ksp") version "1.9.22-1.0.16" apply false
}

repositories {
    google()
    mavenCentral()
}

dependencies1 {    (libs.dagger.hilt.compiler) }

tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}