plugins {
    id("presentation.plugin")
}

android {
    namespace = "ru.crazerr.feature.budgets.presentation"
}

dependencies {
    implementation(project(":feature:budgets:domain"))
    implementation(project(":feature:budgets:data"))
    implementation(project(":feature:budget:presentation"))
}