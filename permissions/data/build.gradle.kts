plugins {
    alias(libs.plugins.androidLibrary)
}

android {
    namespace = "com.cadrikmdev.permissions.data"
    compileSdk = 37

    defaultConfig {
        minSdk = 28
    }
}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.permissions.domain)
}