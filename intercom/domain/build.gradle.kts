plugins {
    alias(libs.plugins.cadrikmdev.jvm.library)
    kotlin("plugin.serialization")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(projects.core.domain)
}