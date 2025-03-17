plugins {
    id("presentation.plugin")
}

android {
    namespace = "ru.crazerr.feature.budget.presentation"
}

dependencies {
    implementation(project(":feature:budget:data"))
    implementation(project(":feature:budget:domain"))
    implementation(project(":feature:category:presentation"))
}