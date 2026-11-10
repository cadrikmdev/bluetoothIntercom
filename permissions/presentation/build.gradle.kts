plugins {
    alias(libs.plugins.cadrikmdev.android.feature.ui)
}

android {
    namespace = "com.cadrikmdev.permissions.presentation"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.google.maps.android.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.common.ktx)
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.timber)

    implementation(projects.core.domain)
    implementation(projects.permissions.domain)
}