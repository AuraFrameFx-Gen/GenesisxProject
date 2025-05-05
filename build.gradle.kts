// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        classpath(libs.plugins.agp.get())
        classpath(libs.plugins.kotlin.android)
        classpath(libs.plugins.kotlin.parcelize)
        classpath(libs.plugins.ksp)
        classpath(libs.plugins.hilt.android)

        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle.kts files
    }
}

// Configure all projects
tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}

// Configure all projects
allprojects {
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://oss.sonatype.org/content/repositories/snapshots/") }
    }

    // Configure all projects with common settings
    afterEvaluate {
        if (plugins.hasPlugin("com.android.application") ||
            plugins.hasPlugin("com.android.library") ||
            plugins.hasPlugin("com.android.dynamic-feature")
        ) {

            // Configure Android specific settings
            extensions.configure<com.android.build.gradle.BaseExtension> {
                compileSdkVersion(34)

                defaultConfig {
                    minSdk = 31
                    targetSdk = 34

                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

                    // Enable vector drawable support
                    vectorDrawables.useSupportLibrary = true
                }

                compileOptions {
                    sourceCompatibility = JavaVersion.VERSION_17
                    targetCompatibility = JavaVersion.VERSION_17
                }

                kotlinOptions {
                    jvmTarget = JavaVersion.VERSION_17.toString()
                    freeCompilerArgs = freeCompilerArgs + listOf(
                        "-opt-in=kotlin.RequiresOptIn",
                        "-Xjvm-default=all"
                    )
                }

                // Enable view binding
                buildFeatures {
                    viewBinding = true
                    buildConfig = true
                }

                // Enable data binding
                buildFeatures.dataBinding = true

                // Packaging options
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
                    }
                }
            }
        }
    }
}

// Task to print project dependencies
tasks.register("printDependencies") {
    group = "help"
    description = "Print project dependencies"
    doLast {
        rootProject.allprojects.forEach { project ->
            println("\nProject: ${project.name}")
            println("-------------------")
            project.configurations
                .filter { it.isCanBeResolved }
                .forEach { configuration ->
                    try {
                        println("\nConfiguration: ${configuration.name}")
                        configuration.resolvedConfiguration.resolvedArtifacts.forEach { artifact ->
                            println("- ${artifact.moduleVersion.id}")
                        }
                    } catch (e: Exception) {
                        // Ignore configurations that can't be resolved
                    }
                }
        }
    }
}