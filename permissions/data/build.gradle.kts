plugins {
    alias(libs.plugins.cadrikmdev.android.library)
}

android {
    namespace = "com.cadrikmdev.permissions.data"
}

dependencies {

    implementation(projects.core.domain)
    implementation(projects.permissions.domain)
}