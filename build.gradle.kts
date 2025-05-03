plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.ksp) apply false
}

// Clean build directory
tasks.register("clean", Delete::class) {
    delete(layout.buildDirectory)
}
