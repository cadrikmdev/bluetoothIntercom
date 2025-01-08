plugins {
    alias(libs.plugins.cadrikmdev.android.feature.ui)
    alias(libs.plugins.mapsplatform.secrets.plugin)
}

android {
    namespace = "com.cadrikmdev.intercom.presentation"
}

dependencies {

    implementation(libs.coil.compose)
    implementation(libs.google.maps.android.compose)
    implementation(libs.androidx.activity.compose)
    implementation(libs.timber)
    implementation(libs.preference.library)

    implementation(projects.intercom.domain)
}