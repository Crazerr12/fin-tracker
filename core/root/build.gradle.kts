plugins {
    id("root.plugin")
}

android {
    namespace = "ru.crazerr.core.root"
}

dependencies {
    implementation(project(":core:utils"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}