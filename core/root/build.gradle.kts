plugins {
    id("root.plugin")
}

android {
    namespace = "ru.crazerr.core.root"
}

dependencies {
    implementation(project(":core:utils"))
    implementation(project(":feature:main:presentation"))
    implementation(project(":feature:transactions:presentation"))
    implementation(project(":feature:transaction:domain:api"))
    implementation(project(":feature:budgets:presentation"))
    implementation(project(":feature:analysis:presentation"))
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}