plugins {
    alias(libs.plugins.cadrikmdev.android.library)
    alias(libs.plugins.cadrikmdev.jvm.ktor)
}

android {
    namespace = "com.cadrikmdev.intercom.data"
}

dependencies {
    implementation(libs.timber)
    implementation(libs.bundles.koin)

    implementation(projects.intercom.domain)
    implementation(libs.androidx.core)
}