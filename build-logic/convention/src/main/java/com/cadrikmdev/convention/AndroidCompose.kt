package com.cadrikmdev.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    val localLibs = libs
    commonExtension.run {
        buildFeatures {
            compose = true
        }
        composeOptions {
            kotlinCompilerExtensionVersion = localLibs.findVersion("composeCompiler").get().toString()
        }
        dependencies {
            val bom = localLibs.findLibrary("androidx.compose.bom").get()
            "implementation"(platform(bom))
            "androidTestImplementation"(platform(bom))
            "debugImplementation"(localLibs.findLibrary("androidx.compose.ui.tooling.preview").get())
        }
    }
}
