package com.cadrikmdev.convention

import com.android.build.api.dsl.ApplicationExtension
import com.android.build.api.dsl.BuildType
import com.android.build.api.dsl.CommonExtension
import com.android.build.api.dsl.DynamicFeatureExtension
import com.android.build.api.dsl.LibraryExtension
import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

internal fun Project.configureBuildTypes(
    commonExtension: CommonExtension,
    extensionType: ExtensionType
) {
    commonExtension.buildFeatures.apply { buildConfig = true }
    configureBuildTypesCommon(extensionType)
}

private fun Project.configureBuildTypesCommon(
    extensionType: ExtensionType
) {
    val hostname = gradleLocalProperties(rootDir, providers).getProperty("HOSTNAME") ?: ""
    val speedTestFeatureEnabled =
        gradleLocalProperties(rootDir, providers).getProperty("FEATURE_SPEED_TEST_ENABLED")
            ?.toBoolean() ?: false
    val githubRepoApiUrl =
        gradleLocalProperties(rootDir, providers).getProperty("GITHUB_API_REPO_URL") ?: ""
    val githubAccessToken =
        gradleLocalProperties(rootDir, providers).getProperty("GITHUB_API_TOKEN") ?: ""
    when (extensionType) {
        ExtensionType.APPLICATION -> {
            this@configureBuildTypesCommon.extensions.configure<ApplicationExtension> {
                buildTypes {
                    debug {
                        configureDebugBuildType(
                            hostname,
                            speedTestFeatureEnabled,
                            githubRepoApiUrl,
                            githubAccessToken
                        )
                    }
                    release {
                        configureReleaseBuildType(
                            hostname,
                            speedTestFeatureEnabled,
                            githubRepoApiUrl,
                            githubAccessToken
                        )
                    }
                }
            }
        }

        ExtensionType.LIBRARY -> {
            this@configureBuildTypesCommon.extensions.configure<LibraryExtension> {
                buildTypes {
                    debug {
                        configureDebugBuildType(
                            hostname,
                            speedTestFeatureEnabled,
                            githubRepoApiUrl,
                            githubAccessToken
                        )
                    }
                    release {
                        configureReleaseBuildType(
                            hostname,
                            speedTestFeatureEnabled,
                            githubRepoApiUrl,
                            githubAccessToken
                        )
                    }
                }
            }
        }

        ExtensionType.DYNAMIC_FEATURE -> {
            this@configureBuildTypesCommon.extensions.configure<DynamicFeatureExtension> {
                buildTypes {
                    debug {
                        configureDebugBuildType(
                            hostname,
                            speedTestFeatureEnabled,
                            githubRepoApiUrl,
                            githubAccessToken
                        )
                    }
                    release {
                        configureReleaseBuildType(
                            hostname,
                            speedTestFeatureEnabled,
                            githubRepoApiUrl,
                            githubAccessToken
                        )
                        isMinifyEnabled = false
                    }
                }
            }
        }
    }
}

private fun BuildType.configureDebugBuildType(
    hostname: String,
    speedTestFeatureEnabled: Boolean,
    githubRepoApiUrl: String,
    githubAccessToken: String
) {
    buildConfigField("String", "BASE_URL", "\"$hostname\"")
    buildConfigField("boolean", "FEATURE_SPEED_TEST_ENABLED", "$speedTestFeatureEnabled")
    buildConfigField("String", "GITHUB_API_REPO_URL", "\"$githubRepoApiUrl\"")
    buildConfigField("String", "GITHUB_API_TOKEN", "\"$githubAccessToken\"")
}

private fun BuildType.configureReleaseBuildType(
    hostname: String,
    speedTestFeatureEnabled: Boolean,
    githubRepoApiUrl: String,
    githubAccessToken: String
) {
    buildConfigField("String", "BASE_URL", "\"$hostname\"")
    buildConfigField("boolean", "FEATURE_SPEED_TEST_ENABLED", "$speedTestFeatureEnabled")
    buildConfigField("String", "GITHUB_API_REPO_URL", "\"$githubRepoApiUrl\"")
    buildConfigField("String", "GITHUB_API_TOKEN", "\"$githubAccessToken\"")
    isMinifyEnabled = false
}
