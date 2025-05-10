// Set Android-related system properties in this script
System.setProperty("android.bundle.enableUncompressedNativeLibs", "false")
System.setProperty("android.suppressUnsupportedCompileSdk", "true")
System.setProperty("android.defaults.buildfeatures.buildconfig", "true")
System.setProperty("android.nonTransitiveRClass", "false")
System.setProperty("android.useAndroidX", "true")
System.setProperty("android.disableAutomaticComponentCreation", "true")

// We can't directly use the android {} block in this file
// Instead, use a configuration block that will be applied to the project

// Define a custom task to disable stripping of debug symbols
tasks.whenTaskAdded {
    if (name.startsWith("strip") && name.endsWith("DebugSymbols")) {
        enabled = false
    }
}

// Use afterEvaluate to configure the Android extension after it's available
afterEvaluate {
    android.packageBuildConfig = true
    
    // Disable stripping problematic libraries
    android.packagingOptions.jniLibs.apply {
        // These calls will be applied when the project is evaluated
        excludes.add("**/libAuraFrameFX.so")
        excludes.add("**/libaura-fx-lib.so")
        pickFirsts.add("**/libc++_shared.so")
    }
}
