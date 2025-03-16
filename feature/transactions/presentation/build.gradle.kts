plugins {
    id("presentation.plugin")
}

android {
    namespace = "ru.crazerr.feature.transactions.presentation"

}

dependencies {
    implementation(project(":feature:transactions:data"))
    implementation(project(":feature:transactions:domain"))
    implementation(project(":feature:transaction:presentation"))

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}