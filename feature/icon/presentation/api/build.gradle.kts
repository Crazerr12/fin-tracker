plugins {
    id("presentation.plugin")
}

android {
    namespace = "ru.crazerr.feature.icon.presentation.api"
}

dependencies {
    implementation(project(":feature:icon:domain:api"))
}