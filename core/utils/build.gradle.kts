plugins {
    id("utils.plugin")
}

android {
    namespace = "ru.crazerr.core.utils"
}

dependencies {
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}