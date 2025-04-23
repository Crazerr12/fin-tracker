plugins {
    id("presentation.plugin")
}

android {
    namespace = "ru.crazerr.feature.analysis.presentation"
}

dependencies {
    implementation(project(":feature:analysis:domain"))
    implementation(project(":feature:analysis:data"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}