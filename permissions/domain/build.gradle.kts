plugins {
    alias(libs.plugins.cadrikmdev.jvm.library)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    implementation(projects.core.domain)
}