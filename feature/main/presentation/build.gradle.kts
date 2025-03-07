plugins {
    id("presentation.plugin")
}

android {
    namespace = "ru.crazerr.feature.main.presentation"
}

dependencies {
    implementation(project(":feature:main:data"))
    implementation(project(":feature:main:domain"))
    implementation(project(":feature:account:presentation"))
}