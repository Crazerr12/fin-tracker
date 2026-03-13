plugins {
    id("presentation.plugin")
}

android {
    namespace = "ru.crazerr.feature.settings.presentation"
}

dependencies {
    implementation(project(":feature:category:presentation"))
    implementation(project(":feature:settings:data"))
    implementation(project(":feature:settings:domain"))
    implementation(project(":feature:category:presentation:api"))
}