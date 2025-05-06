import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import org.gradle.kotlin.dsl.withType
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

class CommonConventionsPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            // Kotlin configuration
            tasks.withType<KotlinCompile>().configureEach {
                kotlinOptions {
                    jvmTarget = "22"
                    freeCompilerArgs += listOf(
                        "-Xjvm-default=all",
                        "-opt-in=kotlin.RequiresOptIn",
                        "-Xallow-result-return-type"
                    )
                }
            }

            // Common dependencies
            dependencies {
                add("kotlinCompilerClasspath", kotlin("compiler-embeddable"))
            }
        }
    }
}
