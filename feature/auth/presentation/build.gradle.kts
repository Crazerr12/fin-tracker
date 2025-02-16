plugins {
    id("presentation.plugin")
}

android {
    namespace = "ru.crazerr.feature.auth.presentation"
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}